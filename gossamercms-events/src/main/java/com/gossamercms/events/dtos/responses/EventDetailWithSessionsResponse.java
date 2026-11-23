package com.gossamercms.events.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDetailWithSessionsResponse {
    private UUID id;
    private String title;
    private String startAt;
    private String endAt;
    private String timezone;
    private List<EventTicketTypeDto> eventTicketTypes;
    private List<EventSessionSummaryDto> eventSessions;

    public record EventTicketTypeDto(
            UUID id,
            String name,
            String status,
            String currency,
            Integer quantityAvailable,
            Integer quantityReserved,
            BigDecimal price,
            Integer quantitySold
    ) {}

    public record EventSessionSummaryDto(
            UUID id,
            String title,
            Instant startAt,
            Instant endAt,
            String timezone,
            Integer sortOrder,
            EventLocationDto location,
            List<EventTicketTypeDto> eventTicketTypes
    ) {}

    public record EventLocationDto(
            UUID id,
            UUID eventId,
            String name,
            String locationType,
            String address1,
            String address2,
            String city,
            String stateProvince,
            String postalCode,
            String countryCode,
            BigDecimal latitude,
            BigDecimal longitude,
            String onlineUrl,
            String instructions,
            LocalDateTime deletedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}
