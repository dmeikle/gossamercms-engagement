package com.gossamercms.contact.converters;

import com.gossamercms.contact.data.ContactSubmissionAttachmentsDbService;
import com.gossamercms.contact.dtos.ContactSubmissionAttachmentDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class ContactSubmissionAttachmentConverter
        extends BaseConverter<ContactSubmissionAttachmentDto>
         implements Converter<String, ContactSubmissionAttachmentDto> {

    public ContactSubmissionAttachmentConverter(ContactSubmissionAttachmentsDbService dbService) {
        super(dbService, ContactSubmissionAttachmentDto.class);
    }
}

