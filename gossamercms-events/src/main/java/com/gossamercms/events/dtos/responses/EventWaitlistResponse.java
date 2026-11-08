package com.gossamercms.events.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EventWaitlistResponse {
    private UUID id;
    private UUID eventId;
    private UUID ticketTypeId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer quantityRequested;
    private String status;
    private Integer position;
    private Instant offeredAt;
    private Instant expiresAt;
}
