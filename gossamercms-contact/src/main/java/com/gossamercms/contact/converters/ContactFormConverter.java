package com.gossamercms.contact.converters;

import com.gossamercms.contact.data.ContactFormsDbService;
import com.gossamercms.contact.dtos.ContactFormDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class ContactFormConverter
        extends BaseConverter<ContactFormDto>
         implements Converter<String, ContactFormDto> {

    public ContactFormConverter(ContactFormsDbService dbService) {
        super(dbService, ContactFormDto.class);
    }
}

