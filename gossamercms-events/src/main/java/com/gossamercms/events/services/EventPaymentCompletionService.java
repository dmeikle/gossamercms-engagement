package com.gossamercms.events.services;

import com.gossamercms.events.data.*;
import com.gossamercms.events.dtos.responses.EventOrderResponse;
import com.gossamercms.events.models.EventPayment;
import com.gossamercms.mvc.annotations.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@ModuleService
@RequiredArgsConstructor
public class EventPaymentCompletionService {
    private final EventOrderTransactions transactions;
    private final EventOrdersDbService orders;
    private final EventPaymentsDbService payments;
    private final EventOrderItemsDbService items;
    private final EventTicketTypesDbService tickets;
    private final EventOrderFulfillmentService eventOrderFulfillmentService;

    // Call only after a trusted provider has verified payment or cancellation.
    public void complete(UUID orderId, String provider, String checkoutId,
                         String paymentId, BigDecimal amount, String currency, boolean cancelled) {
        transactions.transaction(() -> {
            var order = orders.order(orderId, true);
            EventPayment payment = payments.payment(orderId);
            if (payment == null || !Objects.equals(provider, payment.getProvider())
                    || !Objects.equals(checkoutId, payment.getProviderCheckoutId())
                    || amount == null || payment.getAmount() == null || order.getTotal() == null
                    || amount.compareTo(payment.getAmount()) != 0 || amount.compareTo(order.getTotal()) != 0
                    || !Objects.equals(currency, payment.getCurrency())
                    || !Objects.equals(currency, order.getCurrency())) {
                throw conflict("Checkout does not match the persisted payment");
            }
            if (cancelled && "cancelled".equals(order.getStatus())
                    && "cancelled".equals(payment.getStatus())) return null;
            if (!cancelled && "paid".equals(order.getStatus()) && "paid".equals(payment.getStatus())
                    && Objects.equals(paymentId, payment.getProviderPaymentId())) return null;
            if (!"pending".equals(order.getStatus()) || !"pending".equals(payment.getStatus()))
                throw conflict("Order is not awaiting payment");
            if (!cancelled && (paymentId == null || paymentId.isBlank() || paymentId.length() > 255))
                throw conflict("A confirmed provider payment ID is required");
            if (!cancelled && order.getExpiresAt() != null && !Instant.now().isBefore(order.getExpiresAt()))
                throw conflict("Checkout has expired");
            List<EventOrderResponse.Item> itemList = items.items(orderId);
            for (var item : itemList.stream()
                    .sorted(Comparator.comparing(item -> item.ticketTypeId().toString())).toList()) {
                tickets.ticket(item.ticketTypeId());
                if (cancelled) tickets.releaseInventory(item.ticketTypeId(), item.quantity());
                else tickets.completeReservation(item.ticketTypeId(), item.quantity());
            }
            if (cancelled) {
                orders.completeCancellation(orderId);
                payments.completeCancellation(orderId);
            } else {
                payments.completePayment(payment.getId(), paymentId);
                orders.completePayment(orderId);

                eventOrderFulfillmentService.fulfill(
                        orders.order(orderId, false),
                        itemList
                );
            }
            return null;
        });
    }

    private static ResponseStatusException conflict(String message) {
        return new ResponseStatusException(HttpStatus.CONFLICT, message);
    }
}
