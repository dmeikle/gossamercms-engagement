package com.gossamercms.events.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
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
    private EventLocationRequest location;
}
