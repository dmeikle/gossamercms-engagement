package com.gossamercms.subscribers.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSubscriptionRequest {
//    private UUID subscriberId;
//    private UUID newsletterId;
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
