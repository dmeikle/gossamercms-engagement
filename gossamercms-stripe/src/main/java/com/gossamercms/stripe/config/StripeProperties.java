package com.gossamercms.stripe.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "gossamercms.stripe.api")
public class StripeProperties {
    private String secretKey;
    private String webhookSecret;
}