package com.gossamercms.contact.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactFormRequest {
    private String name;
    private String slug;
    private String description;
    private String status;
    private String successMessage;
    private List<String> notificationRecipients;
    private Boolean sendConfirmationEmail;
    private String confirmationEmailSubject;
    private String confirmationEmailTemplate;
    private Boolean allowAttachments;
    private Integer maximumAttachments;
    private Integer maximumAttachmentSize;
    private Map<String, Object> settings;
}
