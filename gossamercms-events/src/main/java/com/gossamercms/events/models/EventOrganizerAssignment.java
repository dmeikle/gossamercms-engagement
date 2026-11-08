package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventOrganizerAssignmentDto;
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
public class EventOrganizerAssignment implements BaseModel{
    private UUID id;
    private UUID eventId;
    private UUID organizerId;
    private String role;
    private Boolean isPrimary;
    private Instant createdAt;

    public EventOrganizerAssignmentDto toDto() {
        return EventOrganizerAssignmentDto.builder()
            .id(id)
            .eventId(eventId)
            .organizerId(organizerId)
            .role(role)
            .isPrimary(isPrimary)
            .createdAt(createdAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("event_organizer_assignments")
    .column("id", UUID.class)
    .column("eventId", UUID.class)
    .column("organizerId", UUID.class)
    .column("role", String.class)
    .column("isPrimary", Boolean.class)
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
