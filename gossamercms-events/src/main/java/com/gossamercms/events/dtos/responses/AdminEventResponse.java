package com.gossamercms.events.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class AdminEventResponse {
    private UUID id;
    private String title;
    private String slug;
    private String summary;
    private String description;
    private String eventType;
    private String status;
    private String visibility;
    private Instant startAt;
    private Instant endAt;
    private String timezone;
    private String bannerImageUrl;
    private String thumbnailImageUrl;
    private Instant registrationStartAt;
    private Instant registrationEndAt;
    private Boolean isFeatured;
    private Boolean isPublished;
}
