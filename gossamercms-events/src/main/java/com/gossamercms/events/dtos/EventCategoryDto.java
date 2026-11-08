package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventCategory;
import com.gossamercms.mvc.data.DtoWithId;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCategoryDto implements DtoWithId{
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

    public EventCategory toEntity() {
        return EventCategory.builder()
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
}
