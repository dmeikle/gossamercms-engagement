package com.gossamercms.stripe;

import com.gossamercms.events.config.EventCheckoutProperties;
import com.gossamercms.stripe.adapters.inbound.StripeCheckoutController;
import com.gossamercms.stripe.dtos.requests.CheckoutRequest;
import com.gossamercms.stripe.services.StripeService;
import com.gossamercms.stripe.services.StripeWebhookService;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StripeCheckoutControllerTest {
    @Test
    void usesSuccessAndCancelUrlsFromConfiguration() throws Exception {
        StripeService stripeService = mock(StripeService.class);
        EventCheckoutProperties properties = new EventCheckoutProperties();
        properties.setSuccessUrl("https://localhost:8080/events/checkout/success");
        properties.setCancelUrl("https://localhost:8080/events/checkout/cancel");
        StripeWebhookService webhookService = mock(StripeWebhookService.class);
        when(stripeService.createCheckoutSession(
                2500L, "cad", properties.getSuccessUrl(), properties.getCancelUrl()))
                .thenReturn("https://checkout.stripe.com/session");
        CheckoutRequest request = new CheckoutRequest();
        request.setAmount(2500L);
        request.setCurrency("cad");

        var response = new StripeCheckoutController(stripeService, properties, webhookService)
                .createCheckoutSession(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("https://checkout.stripe.com/session",
                ((Map<?, ?>) response.getBody()).get("url"));
        verify(stripeService).createCheckoutSession(
                2500L, "cad", "https://localhost:8080/events/checkout/success",
                "https://localhost:8080/events/checkout/cancel");
    }

    @Test
    void acceptsStripeWebhookAndDelegatesRawPayloadAndSignature() {
        StripeWebhookService webhookService = mock(StripeWebhookService.class);
        EventCheckoutProperties properties = new EventCheckoutProperties();
        StripeCheckoutController controller = new StripeCheckoutController(
                mock(StripeService.class), properties, webhookService);

        var response = controller.stripeWebhook("{\"id\":\"evt_test\"}", "t=1,v1=signature");

        assertEquals(204, response.getStatusCode().value());
        verify(webhookService).handle("{\"id\":\"evt_test\"}", "t=1,v1=signature");
    }
}
