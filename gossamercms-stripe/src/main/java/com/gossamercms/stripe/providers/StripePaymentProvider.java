package com.gossamercms.stripe.providers;

import com.gossamercms.events.payments.EventPaymentAdapter;
import com.gossamercms.mvc.annotations.ModuleProvider;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@ModuleProvider
@ConditionalOnProperty(name = "gossamercms.stripe.mock.enabled", havingValue = "false", matchIfMissing = true)
public class StripePaymentProvider implements EventPaymentAdapter {

    private final StripeClient stripeClient;

    public StripePaymentProvider(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    @Override
    public String provider() {
        return "stripe";
    }

    @Override
    public Checkout createCheckout(CheckoutRequest request) {
        ValidatedCheckout checkout = validate(request);
        SessionCreateParams.Builder params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCustomerEmail(request.email())
                .setClientReferenceId(request.operationId().toString())
                .setSuccessUrl(request.successUrl())
                .setCancelUrl(request.cancelUrl())
                .putMetadata("order_id", request.operationId().toString())
                .setPaymentIntentData(SessionCreateParams.PaymentIntentData.builder()
                        .putMetadata("order_id", request.operationId().toString())
                        .build());

        for (EventPaymentAdapter.Line line : request.items()) {
            long unitAmount = toMinorUnits(line.unitPrice(), checkout.currencyDigits());
            if (unitAmount == 0) {
                continue;
            }
            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(checkout.currencyCode())
                            .setUnitAmount(unitAmount)
                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Event ticket")
                                    .putMetadata("event_ticket_type_id", line.ticketTypeId().toString())
                                    .build())
                            .build();
            params.addLineItem(SessionCreateParams.LineItem.builder()
                    .setQuantity((long) line.quantity())
                    .setPriceData(priceData)
                    .build());
        }

        try {
            Session session = stripeClient.checkout().sessions().create(
                    params.build(), requestOptions(request.operationId()));
            if (session == null || session.getId() == null || session.getUrl() == null) {
                throw new IllegalStateException("Stripe did not return a checkout session ID and URL");
            }
            return new Checkout(session.getId(), session.getUrl());
        } catch (StripeException exception) {
            throw providerFailure("create Checkout Session", exception);
        }
    }

    @Override
    public boolean cancelCheckout(UUID operationId, String checkoutId) {
        requireOperationId(operationId);
        if (checkoutId == null || checkoutId.isBlank()) {
            throw new IllegalArgumentException("Stripe Checkout Session ID is required");
        }

        try {
            Session session = stripeClient.checkout().sessions().retrieve(checkoutId);
            if (session == null || !operationId.toString().equals(session.getClientReferenceId())) {
                throw new IllegalStateException("Stripe Checkout Session does not match the operation");
            }
            if ("expired".equals(session.getStatus())) {
                return true;
            }
            if (!"open".equals(session.getStatus())) {
                return false;
            }
            Session expired = stripeClient.checkout().sessions().expire(
                    checkoutId, requestOptions(operationId, "expire"));
            return "expired".equals(expired.getStatus());
        } catch (StripeException exception) {
            throw providerFailure("expire Checkout Session", exception);
        }
    }

    @Override
    public EventPaymentAdapter.Refund refund(
            UUID operationId, String paymentId, BigDecimal amount, String currency, String reason) {
        requireOperationId(operationId);
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException("Stripe PaymentIntent ID is required for a refund");
        }
        Currency stripeCurrency = currency(currency);
        long amountInMinorUnits = toMinorUnits(amount, stripeCurrency.getDefaultFractionDigits());
        if (amountInMinorUnits <= 0) {
            throw new IllegalArgumentException("Refund amount must be greater than zero");
        }

        RefundCreateParams.Builder params = RefundCreateParams.builder()
                .setPaymentIntent(paymentId)
                .setAmount(amountInMinorUnits)
                .putMetadata("refund_operation_id", operationId.toString());
        refundReason(reason).ifPresent(params::setReason);

        try {
            com.stripe.model.Refund refund =
                    stripeClient.refunds().create(params.build(), requestOptions(operationId, "refund"));
            if (refund == null || refund.getId() == null) {
                throw new IllegalStateException("Stripe did not return a refund ID");
            }
            return new EventPaymentAdapter.Refund(refund.getId(), "succeeded".equals(refund.getStatus()));
        } catch (StripeException exception) {
            throw providerFailure("create refund", exception);
        }
    }

    private static ValidatedCheckout validate(CheckoutRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Checkout request is required");
        }
        requireOperationId(request.operationId());
        if (request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException("Customer email is required");
        }
        requireHttpUrl(request.successUrl(), "success URL");
        requireHttpUrl(request.cancelUrl(), "cancel URL");

        Currency stripeCurrency = currency(request.currency());
        int currencyDigits = stripeCurrency.getDefaultFractionDigits();
        if (currencyDigits < 0) {
            throw new IllegalArgumentException("Unsupported Stripe currency precision");
        }
        long expectedTotal = toMinorUnits(request.total(), currencyDigits);
        if (expectedTotal <= 0 || request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("A paid checkout must have items and a positive total");
        }

        long lineTotal = 0;
        Set<UUID> ticketTypes = new HashSet<>();
        for (EventPaymentAdapter.Line line : request.items()) {
            if (line == null || line.ticketTypeId() == null || line.quantity() <= 0
                    || line.unitPrice() == null || line.unitPrice().signum() < 0
                    || !ticketTypes.add(line.ticketTypeId())) {
                throw new IllegalArgumentException("Checkout items must have unique ticket types and valid quantities and prices");
            }
            long unitAmount = toMinorUnits(line.unitPrice(), currencyDigits);
            lineTotal = Math.addExact(lineTotal, Math.multiplyExact(unitAmount, line.quantity()));
        }
        if (lineTotal != expectedTotal) {
            throw new IllegalArgumentException("Checkout total does not match the ticket line items");
        }
        return new ValidatedCheckout(stripeCurrency.getCurrencyCode().toLowerCase(Locale.ROOT), currencyDigits);
    }

    private static Currency currency(String currencyCode) {
        if (currencyCode == null || currencyCode.isBlank()) {
            throw new IllegalArgumentException("Currency is required");
        }
        try {
            return Currency.getInstance(currencyCode.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unsupported currency: " + currencyCode, exception);
        }
    }

    private static long toMinorUnits(BigDecimal amount, int fractionDigits) {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("Amount must not be null or negative");
        }
        try {
            return amount.movePointRight(fractionDigits).longValueExact();
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException("Amount has unsupported precision or is too large", exception);
        }
    }

    private static void requireHttpUrl(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Checkout " + label + " is required");
        }
        try {
            String validationValue = value.replace("{CHECKOUT_SESSION_ID}", "checkout_session_id");
            URI uri = URI.create(validationValue);
            if (!uri.isAbsolute() || uri.getHost() == null || uri.getUserInfo() != null
                    || !("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()))) {
                throw new IllegalArgumentException("Checkout " + label + " must be an absolute HTTP(S) URL");
            }
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Checkout " + label + " must be an absolute HTTP(S) URL", exception);
        }
    }

    private static java.util.Optional<RefundCreateParams.Reason> refundReason(String reason) {
        if (reason == null) {
            return java.util.Optional.empty();
        }
        return switch (reason.trim().toLowerCase(Locale.ROOT)) {
            case "duplicate" -> java.util.Optional.of(RefundCreateParams.Reason.DUPLICATE);
            case "fraudulent" -> java.util.Optional.of(RefundCreateParams.Reason.FRAUDULENT);
            case "requested_by_customer" -> java.util.Optional.of(RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER);
            default -> java.util.Optional.empty();
        };
    }

    private static RequestOptions requestOptions(UUID operationId) {
        return requestOptions(operationId, "checkout");
    }

    private static RequestOptions requestOptions(UUID operationId, String action) {
        requireOperationId(operationId);
        return RequestOptions.builder()
                .setIdempotencyKey("gossamer-" + action + "-" + operationId)
                .build();
    }

    private static void requireOperationId(UUID operationId) {
        if (operationId == null) {
            throw new IllegalArgumentException("Operation ID is required");
        }
    }

    private static IllegalStateException providerFailure(String action, StripeException cause) {
        return new IllegalStateException("Stripe could not " + action, cause);
    }

    private record ValidatedCheckout(String currencyCode, int currencyDigits) {}
}
