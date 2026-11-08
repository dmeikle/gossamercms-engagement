package com.gossamercms.events.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class AdminEventRegistrationResponse {
    private UUID id;
    private UUID eventId;
    private UUID orderId;
    private UUID orderItemId;
    private UUID userId;
    private String attendeeFirstName;
    private String attendeeLastName;
    private String attendeeEmail;
    private String status;
    private Instant registeredAt;
    private Instant cancelledAt;
}
