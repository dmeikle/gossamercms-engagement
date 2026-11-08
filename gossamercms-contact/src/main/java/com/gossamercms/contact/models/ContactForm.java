package com.gossamercms.contact.models;

import com.gossamercms.contact.dtos.ContactFormDto;
import com.gossamercms.mvc.helpers.annotations.JsonColumn;
import com.gossamercms.mvc.models.BaseModel;
import com.gossamercms.mvc.models.ModelMeta;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactForm implements BaseModel{
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private String status;
    private String successMessage;
    @JsonColumn
    private List<String> notificationRecipients;
    private Boolean sendConfirmationEmail;
    private String confirmationEmailSubject;
    private String confirmationEmailTemplate;
    private Boolean allowAttachments;
    private Integer maximumAttachments;
    private Integer maximumAttachmentSize;
    @JsonColumn
    private Map<String, Object> settings;
    private UUID createdBy;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public ContactFormDto toDto() {
        return ContactFormDto.builder()
            .id(id)
            .name(name)
            .slug(slug)
            .description(description)
            .status(status)
            .successMessage(successMessage)
            .notificationRecipients(notificationRecipients)
            .sendConfirmationEmail(sendConfirmationEmail)
            .confirmationEmailSubject(confirmationEmailSubject)
            .confirmationEmailTemplate(confirmationEmailTemplate)
            .allowAttachments(allowAttachments)
            .maximumAttachments(maximumAttachments)
            .maximumAttachmentSize(maximumAttachmentSize)
            .settings(settings)
            .createdBy(createdBy)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("contact_forms")
    .column("id", UUID.class)
    .column("name", String.class, 255)
    .column("slug", String.class, 255)
    .column("description", String.class)
    .column("status", String.class, 50)
    .column("successMessage", String.class)
    .column("notificationRecipients", Map.class)
    .column("sendConfirmationEmail", Boolean.class)
    .column("confirmationEmailSubject", String.class, 255)
    .column("confirmationEmailTemplate", String.class)
    .column("allowAttachments", Boolean.class)
    .column("maximumAttachments", Integer.class)
    .column("maximumAttachmentSize", Integer.class)
    .column("settings", Map.class)
    .column("createdBy", UUID.class)
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
