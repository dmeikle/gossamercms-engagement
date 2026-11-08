package com.gossamercms.contact.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactSubmissionResponse {
    private UUID id;
    private UUID contactFormId;
    private String submitterName;
    private String submitterEmail;
    private String subject;
    private Map<String, Object> values;
    private String status;
    private String priority;
    private UUID assignedTo;
    private String sourceUrl;
    private String referrerUrl;
    private String ipAddress;
    private String userAgent;
    private Instant submittedAt;
    private Instant resolvedAt;
}
