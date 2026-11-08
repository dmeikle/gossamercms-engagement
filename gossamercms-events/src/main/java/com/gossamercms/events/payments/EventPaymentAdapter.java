package com.gossamercms.events.payments;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Implementations must use operationId as a provider idempotency key, including retries
 * after timeouts. Never return confirmed results until the provider confirms them.
 * Payment webhooks/reconciliation must use the same database locks as order workflows.
 */
public interface EventPaymentAdapter {
    String provider();

    Checkout createCheckout(CheckoutRequest request);

    boolean cancelCheckout(UUID operationId, String checkoutId);

    Refund refund(UUID operationId, String paymentId, BigDecimal amount, String currency, String reason);

    record Line(UUID ticketTypeId, int quantity, BigDecimal unitPrice) {}

    record CheckoutRequest(UUID operationId, String name, String email, String currency,
                           BigDecimal total, List<Line> items, String successUrl, String cancelUrl) {}

    record Checkout(String checkoutId, String checkoutUrl) {}

    record Refund(String refundId, boolean confirmed) {}
}
