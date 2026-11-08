package com.gossamercms.contact.models;

import com.gossamercms.contact.dtos.ContactFormFieldDto;
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
public class ContactFormField implements BaseModel{
    private UUID id;
    private UUID contactFormId;
    private String name;
    private String label;
    private String type;
    private String placeholder;
    private String helpText;
    private String defaultValue;
    private Boolean isRequired;
    private Boolean isVisible;
    private Boolean isActive;
    private Integer sortOrder;
    @JsonColumn
    private List<Map<String, String>> options;
    @JsonColumn
    private Map<String, Object> validation;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public ContactFormFieldDto toDto() {
        return ContactFormFieldDto.builder()
            .id(id)
            .contactFormId(contactFormId)
            .name(name)
            .label(label)
            .type(type)
            .placeholder(placeholder)
            .helpText(helpText)
            .defaultValue(defaultValue)
            .isRequired(isRequired)
            .isVisible(isVisible)
            .isActive(isActive)
            .sortOrder(sortOrder)
            .options(options)
            .validation(validation)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("contact_form_fields")
    .column("id", UUID.class)
    .column("contactFormId", UUID.class)
    .column("name", String.class, 100)
    .column("label", String.class, 255)
    .column("type", String.class, 50)
    .column("placeholder", String.class, 255)
    .column("helpText", String.class)
    .column("defaultValue", String.class)
    .column("isRequired", Boolean.class)
    .column("isVisible", Boolean.class)
    .column("isActive", Boolean.class)
    .column("sortOrder", Integer.class)
        .column("options", Map.class)
    .column("validation", Map.class)
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
