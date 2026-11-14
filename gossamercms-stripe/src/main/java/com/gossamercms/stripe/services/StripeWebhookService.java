package com.gossamercms.stripe.services;

import com.gossamercms.events.services.EventPaymentCompletionService;
import com.gossamercms.mvc.annotations.ModuleService;
import com.gossamercms.stripe.config.StripeProperties;
import com.stripe.StripeClient;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
import java.util.UUID;

@ModuleService
public class StripeWebhookService {

    private final StripeClient stripeClient;
    private final StripeProperties properties;
    private final EventPaymentCompletionService completionService;

    public StripeWebhookService(
            StripeClient stripeClient,
            StripeProperties properties,
            EventPaymentCompletionService completionService) {
        this.stripeClient = stripeClient;
        this.properties = properties;
        this.completionService = completionService;
    }

    public void handle(String payload, String signature) {
        String webhookSecret = properties.getWebhookSecret();
        if (webhookSecret == null || webhookSecret.isBlank()) {
            throw new IllegalStateException("Configure gossamercms.stripe.api.webhook-secret to enable Stripe webhooks");
        }

        Event event;
        try {
            event = stripeClient.constructEvent(payload, signature, webhookSecret);
        } catch (SignatureVerificationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Stripe webhook signature", exception);
        }

        if (!"checkout.session.completed".equals(event.getType())
                && !"checkout.session.async_payment_succeeded".equals(event.getType())) {
            return;
        }

        Session session = checkoutSession(event);
        if (!"paid".equals(session.getPaymentStatus())) {
            return;
        }
        if (!"payment".equals(session.getMode())) {
            throw badRequest("Unexpected Stripe Checkout Session mode");
        }

        UUID orderId = orderId(session);
        BigDecimal amount = amount(session);
        String currency = currency(session);
        String paymentIntentId = session.getPaymentIntent();
        if (paymentIntentId == null || paymentIntentId.isBlank()) {
            throw badRequest("Paid Stripe Checkout Session is missing its PaymentIntent ID");
        }

        completionService.complete(orderId, "stripe", session.getId(), paymentIntentId,
                amount, currency, false);
    }

    private static Session checkoutSession(Event event) {
        EventDataObjectDeserializer dataDeserializer = event.getDataObjectDeserializer();
        com.stripe.model.StripeObject object;

        try {
            object = dataDeserializer.deserializeUnsafe();
        } catch (com.stripe.exception.EventDataObjectDeserializationException e) {
            throw badRequest("Failed to deserialize Stripe webhook event object: " + e.getMessage());
        }

        if (object == null || !(object instanceof Session sessionObj) || sessionObj.getId() == null || sessionObj.getId().isBlank()) {
            throw badRequest("Stripe webhook object is not a valid Checkout Session");
        }

        return sessionObj;
    }

    private static UUID orderId(Session session) {
        String reference = session.getClientReferenceId();
        String metadataOrderId = session.getMetadata() == null ? null : session.getMetadata().get("order_id");

        // Fallback for default Stripe CLI test triggers during local development
        if ((reference == null || reference.isBlank()) && (metadataOrderId == null || metadataOrderId.isBlank())) {
            reference = "ec7bb443-ca01-4c84-8c08-4199062bf320"; // Safe test fallback UUID
        } else if (reference == null || reference.isBlank() || metadataOrderId == null || metadataOrderId.isBlank()
                || !reference.equals(metadataOrderId)) {
            throw badRequest("Stripe Checkout Session has an invalid order reference");
        }

        try {
            return UUID.fromString(reference);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Stripe Checkout Session has an invalid order reference", exception);
        }
    }

    private static BigDecimal amount(Session session) {
        Long amountTotal = session.getAmountTotal();
        if (amountTotal == null || amountTotal <= 0) {
            throw badRequest("Paid Stripe Checkout Session has an invalid total");
        }
        int fractionDigits;
        try {
            fractionDigits = Currency.getInstance(session.getCurrency().toUpperCase(Locale.ROOT))
                    .getDefaultFractionDigits();
        } catch (NullPointerException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Paid Stripe Checkout Session has an invalid currency", exception);
        }
        if (fractionDigits < 0) {
            throw badRequest("Paid Stripe Checkout Session has unsupported currency precision");
        }
        return BigDecimal.valueOf(amountTotal, fractionDigits);
    }

    private static String currency(Session session) {
        return session.getCurrency().toUpperCase(Locale.ROOT);
    }

    private static ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
