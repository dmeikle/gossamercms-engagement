package com.gossamercms.subscribers.dtos;

import com.gossamercms.subscribers.models.NewsletterSubscription;
import com.gossamercms.mvc.data.DtoWithId;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterSubscriptionDto implements DtoWithId{
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
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public NewsletterSubscription toEntity() {
        return NewsletterSubscription.builder()
            .id(id)
            .subscriberId(subscriberId)
            .newsletterId(newsletterId)
            .status(status)
            .consentType(consentType)
            .consentedAt(consentedAt)
            .consentSource(consentSource)
            .consentText(consentText)
            .subscribedAt(subscribedAt)
            .confirmedAt(confirmedAt)
            .unsubscribedAt(unsubscribedAt)
            .confirmationToken(confirmationToken)
            .confirmationTokenExpiresAt(confirmationTokenExpiresAt)
            .ipAddress(ipAddress)
            .userAgent(userAgent)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
}
