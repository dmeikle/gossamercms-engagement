package com.gossamercms.stripe.config;


import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import com.gossamercms.stripe.providers.StripeMockPaymentProvider;
import com.gossamercms.stripe.adapters.inbound.MockStripeCheckoutController;


@AutoConfiguration
@ConditionalOnProperty(name = "gossamercms.stripe.mock.enabled", havingValue = "true")
@EnableConfigurationProperties(StripeMockProperties.class)
@Import({StripeMockPaymentProvider.class, MockStripeCheckoutController.class})
public class StripeMockAutoConfig {

  @PostConstruct
  public void init() {
    System.out.println("************************************************************");
    System.out.println(">>> StripeMockAutoConfig LOADED");
    System.out.println("************************************************************");
  }

}