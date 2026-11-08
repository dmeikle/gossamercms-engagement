package com.gossamercms.contact.models;

import com.gossamercms.contact.dtos.ContactSubmissionAttachmentDto;
import com.gossamercms.mvc.models.BaseModel;
import com.gossamercms.mvc.models.ModelMeta;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactSubmissionAttachment implements BaseModel{
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

    public ContactSubmissionAttachmentDto toDto() {
        return ContactSubmissionAttachmentDto.builder()
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


public static final ModelMeta META = ModelMeta.builderWithId("contact_submission_attachments")
    .column("id", UUID.class)
    .column("contactSubmissionId", UUID.class)
    .column("fileName", String.class, 255)
    .column("contentType", String.class, 255)
    .column("fileSize", Long.class)
    .column("storageKey", String.class, 1000)
    .column("uploadedBy", UUID.class)
    .column("uploadedAt", Instant.class)
    .column("createdAt", Instant.class)
    .column("deletedAt", Instant.class)
    .defaultSort("id desc")
    .build();



    @Override
    public ModelMeta meta() {
        return META;
    }

    @Override
    public ModelMeta metaOf() {
        return BaseModel.super.metaOf();
    }

}
