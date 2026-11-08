package com.gossamercms.events.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AdminEventOrganizerAssignmentResponse {
    private UUID id;
    private UUID eventId;
    private UUID organizerId;
    private String role;
    private Boolean isPrimary;
}
