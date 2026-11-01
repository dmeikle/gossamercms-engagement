package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventCheckInDto;
import com.gossamercms.events.models.EventCheckIn;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventCheckInsDbService extends BaseDbService<EventCheckIn, EventCheckInDto> {

    public EventCheckInsDbService(DataSourceManager ds) {
        super(EventCheckIn.class, EventCheckInDto.class, ds);
    }

    @Override
    protected EventCheckIn mapToEntity(EventCheckInDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventCheckInDto mapToDto(EventCheckIn entity) {
        return entity.toDto();
    }

    @Override
    protected EventCheckInDto removeExcludedFields(EventCheckInDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
