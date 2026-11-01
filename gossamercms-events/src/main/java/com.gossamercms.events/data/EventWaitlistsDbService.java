package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventWaitlistDto;
import com.gossamercms.events.models.EventWaitlist;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventWaitlistsDbService extends BaseDbService<EventWaitlist, EventWaitlistDto> {

    public EventWaitlistsDbService(DataSourceManager ds) {
        super(EventWaitlist.class, EventWaitlistDto.class, ds);
    }

    @Override
    protected EventWaitlist mapToEntity(EventWaitlistDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventWaitlistDto mapToDto(EventWaitlist entity) {
        return entity.toDto();
    }

    @Override
    protected EventWaitlistDto removeExcludedFields(EventWaitlistDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
