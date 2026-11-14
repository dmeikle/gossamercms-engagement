package com.gossamercms.events.dtos.responses;

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
public class AdminEventSessionListResponse {
    private UUID eventId;
    private UUID locationId;
    private String title;
    private Instant startAt;
    private Instant endAt;
    private String timezone;
    private String status;
}
