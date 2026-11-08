package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventSessionDto;
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
public class EventSession implements BaseModel{
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
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public EventSessionDto toDto() {
        return EventSessionDto.builder()
            .id(id)
            .eventId(eventId)
            .locationId(locationId)
            .title(title)
            .description(description)
            .startAt(startAt)
            .endAt(endAt)
            .timezone(timezone)
            .sortOrder(sortOrder)
            .status(status)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("event_sessions")
    .column("id", UUID.class)
    .column("eventId", UUID.class)
    .column("locationId", UUID.class)
    .column("title", String.class, 255)
    .column("description", String.class)
    .column("startAt", Instant.class)
    .column("endAt", Instant.class)
    .column("timezone", String.class, 100)
    .column("sortOrder", Integer.class)
    .column("status", String.class)
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
