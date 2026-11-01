package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.mvc.models.BaseModel;
import com.gossamercms.mvc.models.ModelMeta;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event implements BaseModel{
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

    public EventDto toDto() {
        return EventDto.builder()
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


public static final ModelMeta META = ModelMeta.builderWithId("events")
    .column("id", UUID.class)
    .column("title", String.class, 255)
    .column("slug", String.class, 255)
    .column("summary", String.class, 500)
    .column("description", String.class)
    .column("eventType", String.class)
    .column("status", String.class)
    .column("visibility", String.class)
    .column("startAt", Instant.class)
    .column("endAt", Instant.class)
    .column("timezone", String.class, 100)
    .column("bannerImageUrl", String.class, 1000)
    .column("thumbnailImageUrl", String.class, 1000)
    .column("registrationStartAt", Instant.class)
    .column("registrationEndAt", Instant.class)
    .column("isFeatured", Boolean.class)
    .column("isPublished", Boolean.class)
    .column("createdBy", UUID.class)
    .column("createdAt", Instant.class)
    .column("updatedAt", Instant.class)
    .column("deletedAt", Instant.class)
    .defaultSort("id desc")
    .build();



    @Override
    public ModelMeta meta() {
        return META;
    }

    @Override
    public ModelMeta metaOf() {
        return BaseModel.super.metaOf();
    }

}
