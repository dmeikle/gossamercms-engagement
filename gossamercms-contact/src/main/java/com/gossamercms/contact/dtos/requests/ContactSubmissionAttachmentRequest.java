package com.gossamercms.contact.dtos.requests;

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
public class ContactSubmissionAttachmentRequest {
    private UUID contactSubmissionId;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private String storageKey;
    private UUID uploadedBy;
    private Instant uploadedAt;
}
