package com.gossamercms.contact.converters;

import com.gossamercms.contact.data.ContactFormFieldsDbService;
import com.gossamercms.contact.dtos.ContactFormFieldDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class ContactFormFieldConverter
        extends BaseConverter<ContactFormFieldDto>
         implements Converter<String, ContactFormFieldDto> {

    public ContactFormFieldConverter(ContactFormFieldsDbService dbService) {
        super(dbService, ContactFormFieldDto.class);
    }
}

