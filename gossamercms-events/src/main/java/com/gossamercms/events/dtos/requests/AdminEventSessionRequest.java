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
public class AdminEventSessionRequest {
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
