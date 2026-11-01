package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.events.models.Event;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventsDbService extends BaseDbService<Event, EventDto> {

    public EventsDbService(DataSourceManager ds) {
        super(Event.class, EventDto.class, ds);
    }

    @Override
    protected Event mapToEntity(EventDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventDto mapToDto(Event entity) {
        return entity.toDto();
    }

    @Override
    protected EventDto removeExcludedFields(EventDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
