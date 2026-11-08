package com.gossamercms.contact.dtos;

import com.gossamercms.contact.models.ContactFormField;
import com.gossamercms.mvc.data.DtoWithId;
import com.gossamercms.mvc.helpers.annotations.JsonColumn;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
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
public class ContactFormFieldDto implements DtoWithId{
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

    public ContactFormField toEntity() {
        return ContactFormField.builder()
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
}
