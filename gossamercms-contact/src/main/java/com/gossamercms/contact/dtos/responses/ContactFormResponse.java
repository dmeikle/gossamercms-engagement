package com.gossamercms.contact.dtos.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactFormResponse {

    private String name;
    private String slug;
    private String description;
    private Boolean allowAttachments;
    private Integer maximumAttachments;
    private Integer maximumAttachmentSize;
    private List<ContactFormFieldResponse> fields;
}
