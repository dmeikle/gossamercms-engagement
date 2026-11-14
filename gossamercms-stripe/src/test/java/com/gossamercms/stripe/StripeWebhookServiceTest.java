package com.gossamercms.stripe;

import com.gossamercms.events.services.EventPaymentCompletionService;
import com.gossamercms.stripe.config.StripeProperties;
import com.gossamercms.stripe.services.StripeWebhookService;
import com.stripe.StripeClient;
import com.stripe.net.Webhook;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StripeWebhookServiceTest {
    private static final String WEBHOOK_SECRET = "whsec_test_secret";

    @Test
    void verifiesSignatureAndCompletesTheMatchingPaidOrder() throws Exception {
        EventPaymentCompletionService completion = mock(EventPaymentCompletionService.class);
        StripeWebhookService service = service(completion);
        UUID orderId = UUID.randomUUID();
        String payload = eventPayload("checkout.session.completed", orderId, "paid");

        service.handle(payload, Webhook.Signature.generateSignatureHeader(payload, WEBHOOK_SECRET));

        verify(completion).complete(orderId, "stripe", "cs_test_checkout", "pi_test_payment",
                new BigDecimal("12.34"), "CAD", false);
    }

    @Test
    void acknowledgesUnpaidCompletedSessionWithoutCompletingTheOrder() throws Exception {
        EventPaymentCompletionService completion = mock(EventPaymentCompletionService.class);
        StripeWebhookService service = service(completion);
        String payload = eventPayload("checkout.session.completed", UUID.randomUUID(), "unpaid");

        assertDoesNotThrow(() ->
                service.handle(payload, Webhook.Signature.generateSignatureHeader(payload, WEBHOOK_SECRET)));

        verifyNoInteractions(completion);
    }

    @Test
    void rejectsInvalidWebhookSignature() {
        StripeWebhookService service = service(mock(EventPaymentCompletionService.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.handle(eventPayload("checkout.session.completed", UUID.randomUUID(), "paid"),
                        "t=1,v1=invalid"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void rejectsOrderReferenceMismatchBeforeCallingCompletion() throws Exception {
        EventPaymentCompletionService completion = mock(EventPaymentCompletionService.class);
        StripeWebhookService service = service(completion);
        UUID referencedOrder = UUID.randomUUID();
        UUID metadataOrder = UUID.randomUUID();
        String payload = eventPayload("checkout.session.completed", referencedOrder, "paid")
                .replace("\"order_id\": \"" + referencedOrder + "\"",
                        "\"order_id\": \"" + metadataOrder + "\"");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.handle(payload, Webhook.Signature.generateSignatureHeader(payload, WEBHOOK_SECRET)));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verifyNoInteractions(completion);
    }

    @Test
    void ignoresOtherVerifiedStripeEvents() throws Exception {
        EventPaymentCompletionService completion = mock(EventPaymentCompletionService.class);
        StripeWebhookService service = service(completion);
        String payload = eventPayload("payment_intent.succeeded", UUID.randomUUID(), "paid");

        service.handle(payload, Webhook.Signature.generateSignatureHeader(payload, WEBHOOK_SECRET));

        verifyNoInteractions(completion);
    }

    private static StripeWebhookService service(EventPaymentCompletionService completion) {
        StripeProperties properties = new StripeProperties();
        properties.setSecretKey("sk_test_for_webhook");
        properties.setWebhookSecret(WEBHOOK_SECRET);
        return new StripeWebhookService(new StripeClient(properties.getSecretKey()), properties, completion);
    }

    private static String eventPayload(String eventType, UUID orderId, String paymentStatus) {
        return """
                {
                  "id": "evt_test_event",
                  "object": "event",
                  "api_version": "2026-10-28.endive",
                  "created": 1794610000,
                  "data": {
                    "object": {
                      "id": "cs_test_checkout",
                      "object": "checkout.session",
                      "client_reference_id": "%s",
                      "amount_total": 1234,
                      "currency": "cad",
                      "payment_status": "%s",
                      "payment_intent": "pi_test_payment",
                      "mode": "payment",
                      "metadata": {"order_id": "%s"}
                    }
                  },
                  "livemode": false,
                  "pending_webhooks": 1,
                  "type": "%s"
                }
                """.formatted(orderId, paymentStatus, orderId, eventType);
    }
}
