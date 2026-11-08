package com.gossamercms.events.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EventTicketResponse {
    private UUID id;
    private UUID registrationId;
    private UUID ticketTypeId;
    private String ticketNumber;
    private String status;
    private Instant issuedAt;
    private Instant transferredAt;
    private Instant cancelledAt;
}
