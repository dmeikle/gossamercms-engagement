package com.gossamercms.events.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCategoryRequest {
    private String name;
    private String slug;
    private String description;
    private UUID parentId;
    private Integer sortOrder;
    private Boolean isActive;
}
