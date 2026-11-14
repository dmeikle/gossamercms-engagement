package com.gossamercms.stripe.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("gossamercms.stripe.mock")
public class StripeMockProperties {
    private String baseUrl;
    private String contextPath = "";
}
