package com.gossamercms.contact.data;

import com.gossamercms.contact.dtos.ContactFormFieldDto;
import com.gossamercms.contact.models.ContactFormField;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class ContactFormFieldsDbService extends BaseDbService<ContactFormField, ContactFormFieldDto> {

    public ContactFormFieldsDbService(DataSourceManager ds) {
        super(ContactFormField.class, ContactFormFieldDto.class, ds);
    }

    @Override
    protected ContactFormField mapToEntity(ContactFormFieldDto dto) {
        return dto.toEntity();
    }

    @Override
    protected ContactFormFieldDto mapToDto(ContactFormField entity) {
        return entity.toDto();
    }

    @Override
    protected ContactFormFieldDto removeExcludedFields(ContactFormFieldDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
