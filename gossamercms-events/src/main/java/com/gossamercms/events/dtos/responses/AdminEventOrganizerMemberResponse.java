package com.gossamercms.events.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class AdminEventOrganizerMemberResponse {
    private UUID id;
    private UUID organizerId;
    private UUID userId;
    private String role;
    private String status;
    private Instant invitedAt;
    private Instant joinedAt;
}
