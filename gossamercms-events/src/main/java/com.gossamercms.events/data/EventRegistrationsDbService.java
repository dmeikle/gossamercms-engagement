package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventRegistrationDto;
import com.gossamercms.events.models.EventRegistration;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventRegistrationsDbService extends BaseDbService<EventRegistration, EventRegistrationDto> {

    public EventRegistrationsDbService(DataSourceManager ds) {
        super(EventRegistration.class, EventRegistrationDto.class, ds);
    }

    @Override
    protected EventRegistration mapToEntity(EventRegistrationDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventRegistrationDto mapToDto(EventRegistration entity) {
        return entity.toDto();
    }

    @Override
    protected EventRegistrationDto removeExcludedFields(EventRegistrationDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
