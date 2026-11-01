package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventSessionDto;
import com.gossamercms.events.models.EventSession;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventSessionsDbService extends BaseDbService<EventSession, EventSessionDto> {

    public EventSessionsDbService(DataSourceManager ds) {
        super(EventSession.class, EventSessionDto.class, ds);
    }

    @Override
    protected EventSession mapToEntity(EventSessionDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventSessionDto mapToDto(EventSession entity) {
        return entity.toDto();
    }

    @Override
    protected EventSessionDto removeExcludedFields(EventSessionDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
