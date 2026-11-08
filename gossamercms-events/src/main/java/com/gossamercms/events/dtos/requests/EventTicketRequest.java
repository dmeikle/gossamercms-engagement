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
public class EventTicketRequest {
    private UUID registrationId;
    private UUID ticketTypeId;
    private String ticketNumber;
    private String qrTokenHash;
    private String status;
    private Instant issuedAt;
    private Instant transferredAt;
    private Instant cancelledAt;
}
