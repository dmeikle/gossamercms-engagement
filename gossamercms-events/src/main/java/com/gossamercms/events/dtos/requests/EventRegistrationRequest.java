package com.gossamercms.events.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRegistrationRequest {
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
