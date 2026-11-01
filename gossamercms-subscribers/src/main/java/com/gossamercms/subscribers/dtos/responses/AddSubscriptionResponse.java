package com.gossamercms.subscribers.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class AddSubscriptionResponse {

    private UUID subscriberId;
    private UUID newsletterId;
    private String status;
    private Instant subscribedAt;
    private String ipAddress;
    private String userAgent;
    private Instant createdAt;
}
