package com.gossamercms.events.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEventOrganizerMemberRequest {
    private UUID organizerId;
    private UUID userId;
    private String role;
    private String status;
    private Instant invitedAt;
    private Instant joinedAt;
}
