package com.gossamercms.stripe.services;

import com.gossamercms.mvc.annotations.ModuleService;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;


@ModuleService
public class StripeService {

    private final StripeClient stripeClient;

    public StripeService(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    public String createCheckoutSession(long amount, String currency, String successUrl, String cancelUrl) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(successUrl)
            .setCancelUrl(cancelUrl)
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(currency)
                            .setUnitAmount(amount) // Amount in cents (e.g., $10.00 = 1000)
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Gossamer CMS Order")
                                    .build()
                            )
                            .build()
                    )
                    .build()
                )
            .build();

        Session session = stripeClient.checkout().sessions().create(params);
        return session.getUrl(); // Returns the hosted Stripe Checkout URL
    }
}