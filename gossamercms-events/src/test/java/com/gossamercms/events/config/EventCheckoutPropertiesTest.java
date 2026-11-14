package com.gossamercms.events.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventCheckoutPropertiesTest {
    @Test
    void bindsHostApplicationRedirectProperties() {
        new ApplicationContextRunner().withUserConfiguration(PropertiesConfig.class)
                .withPropertyValues(
                        "gossamercms.events.checkout.success-url=https://example.com/success",
                        "gossamercms.events.checkout.cancel-url=https://example.com/cancel")
                .run(context -> {
                    var properties = context.getBean(EventCheckoutProperties.class);
                    assertEquals("https://example.com/success", properties.getSuccessUrl());
                    assertEquals("https://example.com/cancel", properties.getCancelUrl());
                });
    }

    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties(EventCheckoutProperties.class)
    static class PropertiesConfig {}
}
