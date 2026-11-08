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
public class EventWaitlistRequest {
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
