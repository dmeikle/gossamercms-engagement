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
public class AdminEventCheckInRequest {
    private UUID ticketId;
    private UUID sessionId;
    private UUID checkedInBy;
    private Instant checkedInAt;
    private String method;
}
