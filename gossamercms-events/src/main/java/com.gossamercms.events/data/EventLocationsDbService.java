package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventLocationDto;
import com.gossamercms.events.models.EventLocation;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventLocationsDbService extends BaseDbService<EventLocation, EventLocationDto> {

    public EventLocationsDbService(DataSourceManager ds) {
        super(EventLocation.class, EventLocationDto.class, ds);
    }

    @Override
    protected EventLocation mapToEntity(EventLocationDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventLocationDto mapToDto(EventLocation entity) {
        return entity.toDto();
    }

    @Override
    protected EventLocationDto removeExcludedFields(EventLocationDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
