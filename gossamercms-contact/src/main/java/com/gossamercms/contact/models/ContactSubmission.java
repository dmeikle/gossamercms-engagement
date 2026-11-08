package com.gossamercms.contact.models;

import com.gossamercms.contact.dtos.ContactSubmissionDto;
import com.gossamercms.mvc.helpers.annotations.JsonColumn;
import com.gossamercms.mvc.models.BaseModel;
import com.gossamercms.mvc.models.ModelMeta;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactSubmission implements BaseModel{
    private UUID id;
    private UUID contactFormId;
    private String submitterName;
    private String submitterEmail;
    private String subject;
    @JsonColumn
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
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public ContactSubmissionDto toDto() {
        return ContactSubmissionDto.builder()
            .id(id)
            .contactFormId(contactFormId)
            .submitterName(submitterName)
            .submitterEmail(submitterEmail)
            .subject(subject)
            .values(values)
            .status(status)
            .priority(priority)
            .assignedTo(assignedTo)
            .sourceUrl(sourceUrl)
            .referrerUrl(referrerUrl)
            .ipAddress(ipAddress)
            .userAgent(userAgent)
            .submittedAt(submittedAt)
            .resolvedAt(resolvedAt)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("contact_submissions")
    .column("id", UUID.class)
    .column("contactFormId", UUID.class)
    .column("submitterName", String.class, 255)
    .column("submitterEmail", String.class, 255)
    .column("subject", String.class, 255)
    .column("values", Map.class)
    .column("status", String.class, 50)
    .column("priority", String.class, 50)
    .column("assignedTo", UUID.class)
    .column("sourceUrl", String.class, 2000)
    .column("referrerUrl", String.class, 2000)
    .column("ipAddress", String.class, 45)
    .column("userAgent", String.class, 1000)
    .column("submittedAt", Instant.class)
    .column("resolvedAt", Instant.class)
    .column("createdAt", Instant.class)
    .column("updatedAt", Instant.class)
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
