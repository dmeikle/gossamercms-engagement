package com.gossamercms.events.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "gossamercms.events.checkout")
public class EventCheckoutProperties {
    private String successUrl;
    private String cancelUrl;
}
