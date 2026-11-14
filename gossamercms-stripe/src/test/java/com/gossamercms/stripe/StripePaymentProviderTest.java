package com.gossamercms.stripe;

import com.gossamercms.events.payments.EventPaymentAdapter;
import com.gossamercms.stripe.providers.StripePaymentProvider;
import com.stripe.StripeClient;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.service.CheckoutService;
import com.stripe.service.RefundService;
import com.stripe.service.checkout.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class StripePaymentProviderTest {
    private final StripeClient stripeClient = mock(StripeClient.class);
    private final CheckoutService checkoutService = mock(CheckoutService.class);
    private final SessionService sessions = mock(SessionService.class);
    private final RefundService refunds = mock(RefundService.class);
    private final StripePaymentProvider provider = new StripePaymentProvider(stripeClient);

    @BeforeEach
    void setUp() {
        when(stripeClient.checkout()).thenReturn(checkoutService);
        when(checkoutService.sessions()).thenReturn(sessions);
        when(stripeClient.refunds()).thenReturn(refunds);
    }

    @Test
    void createsHostedCheckoutSessionWithValidatedLineItemsAndIdempotency() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID paidTicket = UUID.randomUUID();
        UUID freeTicket = UUID.randomUUID();
        Session session = new Session();
        session.setId("cs_test_order");
        session.setUrl("https://checkout.stripe.com/c/pay/cs_test_order");
        when(sessions.create(any(SessionCreateParams.class), any(RequestOptions.class))).thenReturn(session);

        var result = provider.createCheckout(new EventPaymentAdapter.CheckoutRequest(
                orderId, "Buyer", "buyer@example.test", "CAD", new BigDecimal("25.00"),
                List.of(new EventPaymentAdapter.Line(paidTicket, 2, new BigDecimal("12.50")),
                        new EventPaymentAdapter.Line(freeTicket, 1, BigDecimal.ZERO)),
                "https://example.test/success?session_id={CHECKOUT_SESSION_ID}",
                "https://example.test/cancel"));

        assertEquals("cs_test_order", result.checkoutId());
        assertEquals("https://checkout.stripe.com/c/pay/cs_test_order", result.checkoutUrl());
        var params = org.mockito.ArgumentCaptor.forClass(SessionCreateParams.class);
        var options = org.mockito.ArgumentCaptor.forClass(RequestOptions.class);
        verify(sessions).create(params.capture(), options.capture());
        assertEquals(1, params.getValue().getLineItems().size());
        var line = params.getValue().getLineItems().getFirst();
        assertEquals(2L, line.getQuantity());
        assertEquals(1250L, line.getPriceData().getUnitAmount());
        assertEquals("cad", line.getPriceData().getCurrency());
        assertEquals(paidTicket.toString(),
                line.getPriceData().getProductData().getMetadata().get("event_ticket_type_id"));
        assertEquals(orderId.toString(), params.getValue().getMetadata().get("order_id"));
        assertEquals("gossamer-checkout-" + orderId, options.getValue().getIdempotencyKey());
    }

    @Test
    void rejectsTotalMismatchBeforeCallingStripe() {
        var request = new EventPaymentAdapter.CheckoutRequest(
                UUID.randomUUID(), "Buyer", "buyer@example.test", "CAD", new BigDecimal("11.00"),
                List.of(new EventPaymentAdapter.Line(UUID.randomUUID(), 1, new BigDecimal("10.00"))),
                "https://example.test/success", "https://example.test/cancel");

        assertThrows(IllegalArgumentException.class, () -> provider.createCheckout(request));
        verifyNoInteractions(stripeClient);
    }

    @Test
    void expiresOnlyAnOpenSessionAndUsesAStableIdempotencyKey() throws Exception {
        UUID operationId = UUID.randomUUID();
        Session open = new Session();
        open.setStatus("open");
        open.setClientReferenceId(operationId.toString());
        Session expired = new Session();
        expired.setStatus("expired");
        when(sessions.retrieve("cs_test_order")).thenReturn(open);
        when(sessions.expire(eq("cs_test_order"), any(RequestOptions.class))).thenReturn(expired);

        assertTrue(provider.cancelCheckout(operationId, "cs_test_order"));
        var options = org.mockito.ArgumentCaptor.forClass(RequestOptions.class);
        verify(sessions).expire(eq("cs_test_order"), options.capture());
        assertEquals("gossamer-expire-" + operationId, options.getValue().getIdempotencyKey());
    }

    @Test
    void createsAnIdempotentRefundAgainstThePaymentIntent() throws Exception {
        UUID operationId = UUID.randomUUID();
        Refund stripeRefund = new Refund();
        stripeRefund.setId("re_test_refund");
        stripeRefund.setStatus("succeeded");
        when(refunds.create(any(RefundCreateParams.class), any(RequestOptions.class))).thenReturn(stripeRefund);

        EventPaymentAdapter.Refund result = provider.refund(
                operationId, "pi_test_payment", new BigDecimal("10.25"), "CAD", "requested_by_customer");

        assertEquals("re_test_refund", result.refundId());
        assertTrue(result.confirmed());
        var params = org.mockito.ArgumentCaptor.forClass(RefundCreateParams.class);
        var options = org.mockito.ArgumentCaptor.forClass(RequestOptions.class);
        verify(refunds).create(params.capture(), options.capture());
        assertEquals("pi_test_payment", params.getValue().getPaymentIntent());
        assertEquals(1025L, params.getValue().getAmount());
        assertEquals(RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER, params.getValue().getReason());
        assertEquals("gossamer-refund-" + operationId, options.getValue().getIdempotencyKey());
    }
}
