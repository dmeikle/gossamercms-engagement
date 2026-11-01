package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventOrganizerDto;
import com.gossamercms.events.models.EventOrganizer;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventOrganizersDbService extends BaseDbService<EventOrganizer, EventOrganizerDto> {

    public EventOrganizersDbService(DataSourceManager ds) {
        super(EventOrganizer.class, EventOrganizerDto.class, ds);
    }

    @Override
    protected EventOrganizer mapToEntity(EventOrganizerDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventOrganizerDto mapToDto(EventOrganizer entity) {
        return entity.toDto();
    }

    @Override
    protected EventOrganizerDto removeExcludedFields(EventOrganizerDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
