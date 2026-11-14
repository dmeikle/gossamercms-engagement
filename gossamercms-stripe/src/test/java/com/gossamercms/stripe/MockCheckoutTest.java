package com.gossamercms.stripe;

import com.gossamercms.events.config.EventCheckoutProperties;
import com.gossamercms.events.payments.EventPaymentAdapter;
import com.gossamercms.events.services.EventPaymentCompletionService;
import com.gossamercms.stripe.adapters.inbound.MockStripeCheckoutController;
import com.gossamercms.stripe.config.StripeAutoConfig;
import com.gossamercms.stripe.config.StripeMockProperties;
import com.gossamercms.stripe.config.StripeMockAutoConfig;
import com.gossamercms.stripe.providers.StripeMockPaymentProvider;
import com.gossamercms.stripe.providers.StripePaymentProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class MockCheckoutTest {
    @ParameterizedTest
    @CsvSource({
            "http://localhost:8080/api1.0, '', /api1.0/mock-stripe/checkout/",
            "http://localhost:8080/, /api1.0/, /api1.0/mock-stripe/checkout/",
            "http://localhost:8080, '', /mock-stripe/checkout/"
    })
    void buildsCheckoutUrlWithOptionalContextPath(String baseUrl, String contextPath, String expectedPrefix) {
        var properties = new StripeMockProperties();
        properties.setBaseUrl(baseUrl);
        properties.setContextPath(contextPath);
        var provider = new StripeMockPaymentProvider(properties, mock(EventPaymentCompletionService.class));
        var checkout = provider.createCheckout(new EventPaymentAdapter.CheckoutRequest(
                UUID.randomUUID(), "Buyer", "buyer@example.com", "CAD", BigDecimal.TEN,
                List.of(), "http://localhost/success", "http://localhost/cancel"));
        assertTrue(URI.create(checkout.checkoutUrl()).getPath().startsWith(expectedPrefix));
        assertFalse(checkout.checkoutUrl().contains("null"));
    }

    @Test
    void browserCanViewPayAndCancelMockCheckout() throws Exception {
        var completion = mock(EventPaymentCompletionService.class);
        var properties = new StripeMockProperties();
        properties.setBaseUrl("http://localhost:8080/api1.0");
        var provider = new StripeMockPaymentProvider(properties, completion);
        UUID order = UUID.randomUUID();
        var request = new EventPaymentAdapter.CheckoutRequest(order, "Buyer", "buyer@example.com",
                "CAD", BigDecimal.TEN, List.of(), "http://localhost/success", "http://localhost/cancel");
        var checkout = provider.createCheckout(request);
        assertEquals(checkout, provider.createCheckout(request));
        var mvc = standaloneSetup(new MockStripeCheckoutController(provider)).build();
        String path = URI.create(checkout.checkoutUrl()).getPath().substring("/api1.0".length());
        mvc.perform(get(path).requestAttr(
                        org.springframework.security.web.csrf.CsrfToken.class.getName(),
                        new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", "csrf-value")))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("csrf-value")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Simulate successful payment")));
        verifyNoInteractions(completion);
        mvc.perform(post(path).param("action", "pay"))
                .andExpect(status().isSeeOther()).andExpect(redirectedUrl(request.successUrl()));
        verify(completion).complete(order, "stripe-mock", checkout.checkoutId(),
                "mock_pi_" + order, BigDecimal.TEN, "CAD", false);
        UUID cancelledOrder = UUID.randomUUID();
        var cancelRequest = new EventPaymentAdapter.CheckoutRequest(cancelledOrder, request.name(),
                request.email(), request.currency(), request.total(), request.items(),
                request.successUrl(), request.cancelUrl());
        var cancelCheckout = provider.createCheckout(cancelRequest);
        String cancelPath = URI.create(cancelCheckout.checkoutUrl()).getPath().substring("/api1.0".length());
        mvc.perform(post(cancelPath).param("action", "cancel"))
                .andExpect(status().isSeeOther()).andExpect(redirectedUrl(request.cancelUrl()));
        verify(completion).complete(cancelledOrder, "stripe-mock", cancelCheckout.checkoutId(),
                null, BigDecimal.TEN, "CAD", true);
        mvc.perform(get("/mock-stripe/checkout/unknown")).andExpect(status().isNotFound());
        mvc.perform(post(path).param("action", "invalid")).andExpect(status().isBadRequest());
    }

    @Test
    void registersTheRealProviderByDefaultAndTheMockOnlyWhenEnabled() {
        new ApplicationContextRunner().withUserConfiguration(StripeAutoConfig.class)
                .withBean(EventCheckoutProperties.class, EventCheckoutProperties::new)
                .withBean(EventPaymentCompletionService.class, () -> mock(EventPaymentCompletionService.class))
                .withPropertyValues("gossamercms.stripe.api.secret-key=sk_test_context_runner")
                .run(context -> {
                    assertEquals(1, context.getBeansOfType(StripePaymentProvider.class).size());
                    assertTrue(context.getBeansOfType(StripeMockPaymentProvider.class).isEmpty());
                });
        new ApplicationContextRunner().withUserConfiguration(StripeMockAutoConfig.class)
                .withBean(EventPaymentCompletionService.class, () -> mock(EventPaymentCompletionService.class))
                .withPropertyValues("gossamercms.stripe.mock.enabled=true",
                        "gossamercms.stripe.mock.base-url=http://localhost:8080/api1.0")
                .run(context -> {
                    assertEquals(1, context.getBeansOfType(StripeMockPaymentProvider.class).size());
                    assertEquals(1, context.getBeansOfType(MockStripeCheckoutController.class).size());
                });
    }
}
