package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventOrganizerAssignment;
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
public class EventOrganizerAssignmentDto implements DtoWithId{
    private UUID id;
    private UUID eventId;
    private UUID organizerId;
    private String role;
    private Boolean isPrimary;
    private Instant createdAt;

    public EventOrganizerAssignment toEntity() {
        return EventOrganizerAssignment.builder()
            .id(id)
            .eventId(eventId)
            .organizerId(organizerId)
            .role(role)
            .isPrimary(isPrimary)
            .createdAt(createdAt)
            .build();
    }
}
