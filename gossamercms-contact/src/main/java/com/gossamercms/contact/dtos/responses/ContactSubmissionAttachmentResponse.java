package com.gossamercms.contact.dtos.responses;

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
public class ContactSubmissionAttachmentResponse {
    private UUID id;
    private UUID contactSubmissionId;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private String storageKey;
    private UUID uploadedBy;
    private Instant uploadedAt;
}
