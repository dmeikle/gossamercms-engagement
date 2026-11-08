package com.gossamercms.events.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class AdminEventCheckInResponse {
    private UUID id;
    private UUID ticketId;
    private UUID sessionId;
    private UUID checkedInBy;
    private Instant checkedInAt;
    private String method;
}
