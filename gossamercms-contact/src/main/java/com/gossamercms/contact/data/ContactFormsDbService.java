package com.gossamercms.contact.data;

import com.gossamercms.contact.dtos.ContactFormDto;
import com.gossamercms.contact.models.ContactForm;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class ContactFormsDbService extends BaseDbService<ContactForm, ContactFormDto> {

    public ContactFormsDbService(DataSourceManager ds) {
        super(ContactForm.class, ContactFormDto.class, ds);
    }

    @Override
    protected ContactForm mapToEntity(ContactFormDto dto) {
        return dto.toEntity();
    }

    @Override
    protected ContactFormDto mapToDto(ContactForm entity) {
        return entity.toDto();
    }

    @Override
    protected ContactFormDto removeExcludedFields(ContactFormDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
