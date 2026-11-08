package com.gossamercms.contact.dtos.responses;

import com.gossamercms.mvc.helpers.annotations.JsonColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminContactSubmissionListResponse {
    private UUID id;
    private UUID contactFormId;
    private String submitterName;
    private String submitterEmail;
    private String subject;

    private String status;
    private String priority;
    private UUID assignedTo;
    private Instant submittedAt;
    private Instant resolvedAt;

    private String name;
    private String slug;
}
