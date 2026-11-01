package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventCategoryAssignment;
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
public class EventCategoryAssignmentDto implements DtoWithId{
    private UUID id;
    private UUID eventId;
    private UUID categoryId;
    private Instant createdAt;

    public EventCategoryAssignment toEntity() {
        return EventCategoryAssignment.builder()
            .id(id)
            .eventId(eventId)
            .categoryId(categoryId)
            .createdAt(createdAt)
            .build();
    }
}
