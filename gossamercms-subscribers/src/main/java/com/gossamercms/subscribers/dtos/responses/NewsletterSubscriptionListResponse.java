package com.gossamercms.subscribers.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NewsletterSubscriptionListResponse {
    private UUID newsletterId;
    private String newsletterName;
    private UUID subscriptionId;
    private String subscriptionStatus;

}
