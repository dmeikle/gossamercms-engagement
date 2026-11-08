package com.gossamercms.events.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class AdminEventSessionResponse {
    private UUID id;
    private UUID eventId;
    private UUID locationId;
    private String title;
    private String description;
    private Instant startAt;
    private Instant endAt;
    private String timezone;
    private Integer sortOrder;
    private String status;
}
