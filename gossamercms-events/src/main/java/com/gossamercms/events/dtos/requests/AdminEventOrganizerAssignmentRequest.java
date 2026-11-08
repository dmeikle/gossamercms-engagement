package com.gossamercms.events.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEventOrganizerAssignmentRequest {
    private UUID eventId;
    private UUID organizerId;
    private String role;
    private Boolean isPrimary;
}
