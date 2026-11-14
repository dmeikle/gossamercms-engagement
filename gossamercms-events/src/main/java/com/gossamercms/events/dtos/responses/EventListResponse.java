package com.gossamercms.events.dtos.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventListResponse {
    private UUID id;
    private String title;
    private String slug;
    private String summary;
    private String eventType;
    private String status;
    private Instant startAt;
    private Instant endAt;
    private String timezone;
    private String thumbnailImageUrl;
    private Boolean isFeatured;
}
