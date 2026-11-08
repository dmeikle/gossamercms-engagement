package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventCategoryDto;
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
public class EventCategory implements BaseModel{
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private UUID parentId;
    private Integer sortOrder;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public EventCategoryDto toDto() {
        return EventCategoryDto.builder()
            .id(id)
            .name(name)
            .slug(slug)
            .description(description)
            .parentId(parentId)
            .sortOrder(sortOrder)
            .isActive(isActive)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("event_categories")
    .column("id", UUID.class)
    .column("name", String.class, 100)
    .column("slug", String.class, 100)
    .column("description", String.class, 500)
    .column("parentId", UUID.class)
    .column("sortOrder", Integer.class)
    .column("isActive", Boolean.class)
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
