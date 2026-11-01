package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventOrganizerMemberDto;
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
public class EventOrganizerMember implements BaseModel{
    private UUID id;
    private UUID organizerId;
    private UUID userId;
    private String role;
    private String status;
    private Instant invitedAt;
    private Instant joinedAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public EventOrganizerMemberDto toDto() {
        return EventOrganizerMemberDto.builder()
            .id(id)
            .organizerId(organizerId)
            .userId(userId)
            .role(role)
            .status(status)
            .invitedAt(invitedAt)
            .joinedAt(joinedAt)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("event_organizer_members")
    .column("id", UUID.class)
    .column("organizerId", UUID.class)
    .column("userId", UUID.class)
    .column("role", String.class)
    .column("status", String.class)
    .column("invitedAt", Instant.class)
    .column("joinedAt", Instant.class)
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
