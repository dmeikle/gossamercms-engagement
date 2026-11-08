package com.gossamercms.subscribers.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class NewsletterSubscriptionResponse {
    private UUID id;
    private UUID subscriberId;
    private UUID newsletterId;
    private String status;
    private String consentType;
    private Instant consentedAt;
    private String consentSource;
    private String consentText;
    private Instant subscribedAt;
    private Instant confirmedAt;
    private Instant unsubscribedAt;
    private String confirmationToken;
    private Instant confirmationTokenExpiresAt;
    private String ipAddress;
    private String userAgent;
}
