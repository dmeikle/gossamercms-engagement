package com.gossamercms.contact.dtos;

import com.gossamercms.contact.models.ContactSubmissionAttachment;
import com.gossamercms.mvc.data.DtoWithId;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactSubmissionAttachmentDto implements DtoWithId{
    private UUID id;
    private UUID contactSubmissionId;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private String storageKey;
    private UUID uploadedBy;
    private Instant uploadedAt;
    private Instant createdAt;
    private Instant deletedAt;

    public ContactSubmissionAttachment toEntity() {
        return ContactSubmissionAttachment.builder()
            .id(id)
            .contactSubmissionId(contactSubmissionId)
            .fileName(fileName)
            .contentType(contentType)
            .fileSize(fileSize)
            .storageKey(storageKey)
            .uploadedBy(uploadedBy)
            .uploadedAt(uploadedAt)
            .createdAt(createdAt)
            .deletedAt(deletedAt)
            .build();
    }
}
