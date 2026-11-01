package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventCategoryAssignmentDto;
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
public class EventCategoryAssignment implements BaseModel{
    private UUID id;
    private UUID eventId;
    private UUID categoryId;
    private Instant createdAt;

    public EventCategoryAssignmentDto toDto() {
        return EventCategoryAssignmentDto.builder()
            .id(id)
            .eventId(eventId)
            .categoryId(categoryId)
            .createdAt(createdAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("event_category_assignments")
    .column("id", UUID.class)
    .column("eventId", UUID.class)
    .column("categoryId", UUID.class)
    .column("createdAt", Instant.class)
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
