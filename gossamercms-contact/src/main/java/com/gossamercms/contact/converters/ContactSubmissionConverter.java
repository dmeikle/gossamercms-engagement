package com.gossamercms.contact.converters;

import com.gossamercms.contact.data.ContactSubmissionsDbService;
import com.gossamercms.contact.dtos.ContactSubmissionDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class ContactSubmissionConverter
        extends BaseConverter<ContactSubmissionDto>
         implements Converter<String, ContactSubmissionDto> {

    public ContactSubmissionConverter(ContactSubmissionsDbService dbService) {
        super(dbService, ContactSubmissionDto.class);
    }
}

