package com.gossamercms.events.dtos.requests;

import java.util.UUID;

public record EventOrganizerMemberRequest(UUID userId, String role) {
}
