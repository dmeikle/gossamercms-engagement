package com.gossamercms.contact.dtos;

import com.gossamercms.contact.models.ContactSubmission;
import com.gossamercms.mvc.data.DtoWithId;
import com.gossamercms.mvc.helpers.annotations.JsonColumn;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactSubmissionDto implements DtoWithId{
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

    public ContactSubmission toEntity() {
        return ContactSubmission.builder()
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
}
