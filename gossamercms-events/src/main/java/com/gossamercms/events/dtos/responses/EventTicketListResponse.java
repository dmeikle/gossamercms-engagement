package com.gossamercms.events.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventTicketListResponse {

    private UUID registrationId;
    private Instant registeredAt;
    private Instant cancelledAt;
    private UUID eventId;
    private String title;
    private String slug;
    private String summary;
    private String eventType;
    private Instant startAt;
    private Instant endAt;
    private String timezone;
    private String thumbnailImageUrl;
    private Boolean isFeatured;
    private UUID ticketId;
    private UUID ticketTypeId;
    private String ticketNumber;
    private String qrTokenHash;
    private String status;
    private Instant issuedAt;
    private BigDecimal total;
}