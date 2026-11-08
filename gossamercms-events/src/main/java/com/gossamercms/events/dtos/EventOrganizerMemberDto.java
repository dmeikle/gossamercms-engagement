package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventOrganizerMember;
import com.gossamercms.mvc.data.DtoWithId;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventOrganizerMemberDto implements DtoWithId{
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

    public EventOrganizerMember toEntity() {
        return EventOrganizerMember.builder()
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
}
