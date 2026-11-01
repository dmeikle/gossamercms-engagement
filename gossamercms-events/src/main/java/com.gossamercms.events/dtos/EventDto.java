package com.gossamercms.events.dtos;

import com.gossamercms.events.models.Event;
import com.gossamercms.mvc.data.DtoWithId;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto implements DtoWithId{
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
    private UUID createdBy;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public Event toEntity() {
        return Event.builder()
            .id(id)
            .title(title)
            .slug(slug)
            .summary(summary)
            .description(description)
            .eventType(eventType)
            .status(status)
            .visibility(visibility)
            .startAt(startAt)
            .endAt(endAt)
            .timezone(timezone)
            .bannerImageUrl(bannerImageUrl)
            .thumbnailImageUrl(thumbnailImageUrl)
            .registrationStartAt(registrationStartAt)
            .registrationEndAt(registrationEndAt)
            .isFeatured(isFeatured)
            .isPublished(isPublished)
            .createdBy(createdBy)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
}
