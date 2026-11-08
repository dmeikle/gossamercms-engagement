package com.gossamercms.events.services;

import com.gossamercms.events.data.*;
import com.gossamercms.events.dtos.EventOrderDto;
import com.gossamercms.events.dtos.requests.*;
import com.gossamercms.events.dtos.responses.*;
import com.gossamercms.events.models.EventPayment;
import com.gossamercms.events.models.EventRefund;
import com.gossamercms.events.models.EventTicketType;
import com.gossamercms.events.payments.EventPaymentAdapter;
import com.gossamercms.events.payments.PaymentAdapterUnavailableException;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import com.gossamercms.mvc.annotations.ModuleService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@ModuleService
public class EventOrderService {
    private final EventOrdersDbService orders;
    private final EventOrderItemsDbService items;
    private final EventPaymentsDbService payments;
    private final EventRefundsDbService refunds;
    private final EventTicketTypesDbService tickets;
    private final EventsDbService events;
    private final EventOrderTransactions transactions;
    private final ObjectProvider<EventPaymentAdapter> adapters;
    private final SecureRandom random = new SecureRandom();

    public EventOrderService(EventOrdersDbService orders, EventOrderItemsDbService items,
                             EventPaymentsDbService payments, EventRefundsDbService refunds,
                             EventTicketTypesDbService tickets, EventsDbService events,
                             EventOrderTransactions transactions, ObjectProvider<EventPaymentAdapter> adapters) {
        this.orders = orders;
        this.items = items;
        this.payments = payments;
        this.refunds = refunds;
        this.tickets = tickets;
        this.events = events;
        this.transactions = transactions;
        this.adapters = adapters;
    }

    public UUID authenticated(CurrentJwtUser user) {
        if (user == null || !user.isAuthenticated() || user.getUserId() == null)
            throw error(HttpStatus.UNAUTHORIZED, "Authentication required");
        return user.getUserId();
    }

    public void requireManager(CurrentJwtUser user, UUID eventId) {
        if (!events.canManage(eventId, authenticated(user)))
            throw error(HttpStatus.FORBIDDEN, "Event management permission required");
    }

    private void authorize(CurrentJwtUser user, EventOrderDto order) {
        UUID userId = authenticated(user);
        if (!userId.equals(order.getPurchaserUserId()) && !events.canManage(order.getEventId(), userId))
            throw error(HttpStatus.NOT_FOUND, "Order not found");
    }

    public EventOrderResponse read(CurrentJwtUser user, UUID id) {
        return response(authorizedOrder(user, id));
    }

    public EventOrderDto authorizedOrder(CurrentJwtUser user, UUID id) {
        authenticated(user);
        var order = orders.order(id, false);
        authorize(user, order);
        return order;
    }

    public PublicEventOrderResponse publicRead(String token) {
        if (token == null || !token.matches("[A-Za-z0-9_-]{43}"))
            throw error(HttpStatus.NOT_FOUND, "Order not found");
        var order = orders.publicOrder(hash(token));
        return new PublicEventOrderResponse(order.getEventId(), order.getCurrency(), order.getTotal(), order.getStatus());
    }

    public EventOrderResponse response(EventOrderDto order) {
        return EventOrderResponse.from(order, items.items(order.getId()));
    }

    public EventOrderCheckoutResponse checkout(CurrentJwtUser user, UUID eventId, EventOrderRequest request) {
        UUID userId = authenticated(user);
        validate(request);
        EventPaymentAdapter adapter = defaultAdapter();
        UUID orderId = UUID.randomUUID();
        UUID paymentId = UUID.randomUUID();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        var lines = transactions.transaction(() -> {
            events.requireEventOnSale(eventId);
            List<EventPaymentAdapter.Line> priced = new ArrayList<>();
            BigDecimal subtotal = BigDecimal.ZERO;
            for (var item : request.items().stream()
                    .sorted(Comparator.comparing(item -> item.ticketTypeId().toString())).toList()) {
                var ticket = tickets.ticket(item.ticketTypeId());
                validateTicket(ticket, eventId, item.quantity(), request.currency());
                BigDecimal price = ticket.getPrice();
                subtotal = subtotal.add(price.multiply(BigDecimal.valueOf(item.quantity())));
                priced.add(new EventPaymentAdapter.Line(item.ticketTypeId(), item.quantity(), price));
                tickets.reserveInventory(item.ticketTypeId(), item.quantity());
            }
            orders.createCheckout(EventOrderDto.builder().id(orderId).eventId(eventId).orderNumber("EO-" + orderId)
                    .purchaserUserId(userId).purchaserName(request.name().trim()).purchaserEmail(request.email().trim())
                    .currency(request.currency()).total(subtotal)
                    .expiresAt(Instant.now().plus(30, ChronoUnit.MINUTES)).build());
            for (var line : priced) {
                items.createLine(UUID.randomUUID(), orderId, line.ticketTypeId(), line.quantity(), line.unitPrice());
            }
            payments.createCheckout(paymentId, orderId, adapter.provider(), subtotal, request.currency());
            orders.createPublicAccess(orderId, hash(token), Instant.now().plus(30, ChronoUnit.DAYS));
            return List.copyOf(priced);
        });

        EventPaymentAdapter.Checkout checkout;
        try {
            checkout = adapter.createCheckout(new EventPaymentAdapter.CheckoutRequest(orderId,
                    request.name().trim(), request.email().trim(), request.currency(),
                    lines.stream().map(line -> line.unitPrice().multiply(BigDecimal.valueOf(line.quantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add),
                    lines, request.successUrl(), request.cancelUrl()));
            if (checkout == null || blank(checkout.checkoutId()) || checkout.checkoutId().length() > 255
                    || !httpUrl(checkout.checkoutUrl()))
                throw error(HttpStatus.BAD_GATEWAY, "Provider did not return a checkout session");
        } catch (RuntimeException failure) {
            transactions.transaction(() -> {
                orders.order(orderId, true);
                orders.markCheckoutUnknown(orderId);
                payments.markCheckoutUnknown(paymentId);
                return null;
            });
            throw error(HttpStatus.BAD_GATEWAY, "Checkout outcome is unknown; reconciliation required for order " + orderId);
        }
        transactions.transaction(() -> {
            var order = orders.order(orderId, true);
            payments.payment(orderId);
            payments.completeCheckout(paymentId, checkout.checkoutId());
            if ("checkout_creating".equals(order.getStatus())) orders.setStatus(orderId, "pending");
            return null;
        });
        return new EventOrderCheckoutResponse(read(user, orderId), checkout.checkoutUrl(), token);
    }

    public EventOrderResponse cancel(CurrentJwtUser user, UUID id, EventOrderCancelRequest request) {
        authenticated(user);
        reason(request == null ? null : request.reason());
        EventPayment payment = transactions.transaction(() -> {
            var order = orders.order(id, true);
            authorize(user, order);
            if ("cancelled".equals(order.getStatus())) return null;
            if (!Set.of("pending", "cancelling").contains(order.getStatus()))
                throw error(HttpStatus.CONFLICT, "Only pending orders can be cancelled");
            var existingPayment = payments.payment(id);
            if (existingPayment != null && !blank(existingPayment.getProviderCheckoutId())) {
                adapter(existingPayment.getProvider());
                if (!Set.of("pending", "cancelling").contains(existingPayment.getStatus()))
                    throw error(HttpStatus.CONFLICT, "Payment is not pending");
            } else if (existingPayment != null && !"pending".equals(existingPayment.getStatus())) {
                throw error(HttpStatus.CONFLICT, "Checkout needs reconciliation");
            }
            orders.claimCancellation(id, request.reason().trim());
            return existingPayment;
        });
        if (payment != null && !blank(payment.getProviderCheckoutId())) {
            try {
                if (!adapter(payment.getProvider()).cancelCheckout(id, payment.getProviderCheckoutId()))
                    throw error(HttpStatus.BAD_GATEWAY, "Checkout cancellation was not confirmed");
            } catch (PaymentAdapterUnavailableException unavailable) {
                throw unavailable;
            } catch (RuntimeException failure) {
                throw error(HttpStatus.BAD_GATEWAY, "Checkout cancellation is unconfirmed; retry the cancellation");
            }
        }
        return transactions.transaction(() -> {
            var order = orders.order(id, true);
            if ("cancelled".equals(order.getStatus())) return response(order);
            if (!"cancelling".equals(order.getStatus()))
                throw error(HttpStatus.CONFLICT, "Order state changed; reconciliation required");
            releaseInventory(id);
            orders.completeCancellation(id);
            payments.completeCancellation(id);
            return response(orders.order(id, false));
        });
    }

    private record RefundOperation(EventRefund refund, EventPayment payment) {}

    public EventOrderRefundResponse refund(CurrentJwtUser user, UUID id, EventOrderRefundRequest request) {
        authenticated(user);
        reason(request == null ? null : request.reason());
        if (request.amount() == null || request.amount().signum() <= 0)
            throw error(HttpStatus.BAD_REQUEST, "Refund amount must be positive");
        RefundOperation operation = transactions.transaction(() -> {
            var order = orders.order(id, true);
            authorize(user, order);
            if (!Set.of("paid", "partially_refunded", "refund_processing").contains(order.getStatus()))
                throw error(HttpStatus.CONFLICT, "Order is not refundable");
            money(request.amount(), order.getCurrency());
            var payment = payments.payment(id);
            if (payment == null || blank(payment.getProviderPaymentId())
                    || !Set.of("paid", "succeeded", "partially_refunded").contains(payment.getStatus())
                    || payment.getAmount() == null || !Objects.equals(order.getCurrency(), payment.getCurrency()))
                throw error(HttpStatus.CONFLICT, "A confirmed payment is required");
            adapter(payment.getProvider());
            if (refunds.refundHistoryNeedsReconciliation(payment.getId()))
                throw error(HttpStatus.CONFLICT, "Existing refunds need reconciliation");
            var existing = refunds.inflightRefund(payment.getId());
            if (existing != null) {
                if (existing.getAmount().compareTo(request.amount()) != 0
                        || !Objects.equals(existing.getReason(), request.reason().trim()))
                    throw error(HttpStatus.CONFLICT, "Another refund is in progress");
                return new RefundOperation(existing, payment);
            }
            BigDecimal limit = payment.getAmount().min(order.getTotal()).subtract(refunds.refunded(payment.getId()));
            if (request.amount().compareTo(limit) > 0)
                throw error(HttpStatus.CONFLICT, "Refund exceeds remaining paid amount");
            var refund = EventRefund.builder().id(UUID.randomUUID()).paymentId(payment.getId())
                    .amount(request.amount()).currency(payment.getCurrency()).reason(request.reason().trim())
                    .status("processing").build();
            refunds.createProcessing(refund);
            orders.setStatus(id, "refund_processing");
            return new RefundOperation(refund, payment);
        });
        var refund = operation.refund();
        EventPaymentAdapter.Refund result;
        try {
            result = adapter(operation.payment().getProvider()).refund(refund.getId(),
                    operation.payment().getProviderPaymentId(), refund.getAmount(), refund.getCurrency(), refund.getReason());
            if (result == null || !result.confirmed() || blank(result.refundId()) || result.refundId().length() > 255)
                throw error(HttpStatus.BAD_GATEWAY, "Refund was not confirmed");
        } catch (PaymentAdapterUnavailableException unavailable) {
            throw unavailable;
        } catch (RuntimeException failure) {
            throw error(HttpStatus.BAD_GATEWAY, "Refund is unconfirmed; retry the same amount and reason");
        }
        return transactions.transaction(() -> {
            var order = orders.order(id, true);
            var payment = payments.payment(id);
            var inflight = refunds.inflightRefund(payment.getId());
            if (inflight != null && refund.getId().equals(inflight.getId())) {
                refunds.completeRefund(refund.getId(), result.refundId());
                BigDecimal refunded = refunds.refunded(payment.getId());
                String status = refunded.compareTo(order.getTotal().min(payment.getAmount())) >= 0
                        ? "refunded" : "partially_refunded";
                orders.setStatus(id, status);
                payments.setStatus(payment.getId(), status);
            }
            return new EventOrderRefundResponse(refund.getId(), refund.getAmount(), refund.getCurrency(), "refunded");
        });
    }

    private void releaseInventory(UUID orderId) {
        // Stable lock order matches checkout; the order lock prevents double-release.
        for (var item : items.items(orderId).stream()
                .sorted(Comparator.comparing(item -> item.ticketTypeId().toString())).toList()) {
            tickets.ticket(item.ticketTypeId());
            tickets.releaseInventory(item.ticketTypeId(), item.quantity());
        }
    }

    private EventPaymentAdapter defaultAdapter() {
        EventPaymentAdapter selected = adapters.getIfUnique();
        if (selected == null || blank(selected.provider())) throw new PaymentAdapterUnavailableException();
        return selected;
    }

    private EventPaymentAdapter adapter(String provider) {
        if (blank(provider)) throw new PaymentAdapterUnavailableException();
        var matching = adapters.stream().filter(a -> provider.equals(a.provider())).toList();
        if (matching.size() != 1) throw new PaymentAdapterUnavailableException();
        return matching.getFirst();
    }

    private static void validate(EventOrderRequest request) {
        if (request == null || blank(request.name()) || request.name().trim().length() > 255
                || blank(request.email()) || request.email().length() > 255
                || !request.email().trim().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+"))
            throw error(HttpStatus.BAD_REQUEST, "Valid name and email are required");
        currency(request.currency());
        if (!httpUrl(request.successUrl()) || !httpUrl(request.cancelUrl()))
            throw error(HttpStatus.BAD_REQUEST, "Valid HTTP(S) successUrl and cancelUrl are required");
        if (request.items() == null || request.items().isEmpty() || request.items().size() > 100)
            throw error(HttpStatus.BAD_REQUEST, "Between 1 and 100 items are required");
        Set<UUID> seen = new HashSet<>();
        for (var item : request.items()) {
            if (item == null || item.ticketTypeId() == null || item.quantity() == null
                    || item.quantity() <= 0 || !seen.add(item.ticketTypeId()))
                throw error(HttpStatus.BAD_REQUEST, "Items need distinct ticket types and positive quantities");
        }
    }

    private static void validateTicket(EventTicketType ticket, UUID event, int quantity, String currency) {
        Instant now = Instant.now();
        if (ticket.getDeletedAt() != null || !event.equals(ticket.getEventId()) || !currency.equals(ticket.getCurrency())
                || ticket.getPrice() == null || ticket.getPrice().signum() < 0
                || ticket.getStatus() == null || !Set.of("active", "on_sale").contains(ticket.getStatus().toLowerCase(Locale.ROOT))
                || !Boolean.TRUE.equals(ticket.getIsVisible())
                || (ticket.getSalesStartAt() != null && now.isBefore(ticket.getSalesStartAt()))
                || (ticket.getSalesEndAt() != null && !now.isBefore(ticket.getSalesEndAt()))
                || quantity < (ticket.getMinimumPerOrder() == null ? 1 : ticket.getMinimumPerOrder())
                || quantity > (ticket.getMaximumPerOrder() == null ? Integer.MAX_VALUE : ticket.getMaximumPerOrder()))
            throw error(HttpStatus.CONFLICT, "Ticket type is not available for this order");
        money(ticket.getPrice(), currency);
        long reserved = ticket.getQuantityReserved() == null ? 0 : ticket.getQuantityReserved();
        long sold = ticket.getQuantitySold() == null ? 0 : ticket.getQuantitySold();
        if (reserved < 0 || sold < 0 || reserved + quantity > Integer.MAX_VALUE
                || (ticket.getQuantityAvailable() != null && reserved + sold + quantity > ticket.getQuantityAvailable()))
            throw error(HttpStatus.CONFLICT, "Insufficient ticket availability");
    }

    private static Currency currency(String code) {
        try {
            if (code == null || !code.matches("[A-Z]{3}")) throw new IllegalArgumentException();
            Currency currency = Currency.getInstance(code);
            if (currency.getDefaultFractionDigits() < 0) throw new IllegalArgumentException();
            return currency;
        } catch (IllegalArgumentException failure) {
            throw error(HttpStatus.BAD_REQUEST, "An ISO currency code is required");
        }
    }

    private static void money(BigDecimal amount, String code) {
        if (amount.stripTrailingZeros().scale() > currency(code).getDefaultFractionDigits())
            throw error(HttpStatus.BAD_REQUEST, "Amount has invalid currency precision");
    }

    private static void reason(String reason) {
        if (blank(reason) || reason.trim().length() > 500)
            throw error(HttpStatus.BAD_REQUEST, "Reason is required (maximum 500 characters)");
    }

    private static boolean blank(String value) {
        return value == null || value.isBlank();
    }

    private static boolean httpUrl(String value) {
        if (value == null || value.length() > 2000) return false;
        try {
            URI uri = URI.create(value);
            return Set.of("https", "http").contains(uri.getScheme()) && uri.getHost() != null
                    && uri.getUserInfo() == null;
        } catch (IllegalArgumentException failure) {
            return false;
        }
    }

    private static String hash(String token) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.US_ASCII)));
        } catch (NoSuchAlgorithmException impossible) {
            throw new IllegalStateException(impossible);
        }
    }

    private static ResponseStatusException error(HttpStatus status, String reason) {
        return new ResponseStatusException(status, reason);
    }
}
