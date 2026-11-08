package com.gossamercms.contact.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactFormFieldResponse {
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
    private List<Map<String, String>> options;
    private Map<String, Object> validation;
}
