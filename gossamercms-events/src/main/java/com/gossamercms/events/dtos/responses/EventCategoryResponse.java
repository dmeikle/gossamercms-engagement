package com.gossamercms.events.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCategoryResponse {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private UUID parentId;
    private Integer sortOrder;
    private Boolean isActive;
}
