package com.gossamercms.events.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventTicketListResponse {

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
    private List<Ticket> tickets;
    private List<Session> sessions;

    public record Ticket(
            UUID ticketId,
            String orderNumber,
            BigDecimal total,
            String currency,
            Instant paidAt,
            String eventTicketTypeName,
            String ticketTypeName,
            UUID registrationId,
            Instant registeredAt,
            Instant cancelledAt,
            UUID eventId,
            UUID ticketTypeId,
            String ticketNumber,
            String qrTokenHash,
            String status,
            Instant issuedAt
    ) {}

    public record Session(
            UUID id,
            String title,
            Instant startAt,
            Instant endAt,
            String timezone,
            Integer sortOrder,
            EventDetailWithSessionsResponse.EventLocationDto location,
            List<Ticket> tickets
    ) {}
}