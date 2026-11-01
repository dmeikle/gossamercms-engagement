package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventTicketTypeDto;
import com.gossamercms.events.models.EventTicketType;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventTicketTypesDbService extends BaseDbService<EventTicketType, EventTicketTypeDto> {

    public EventTicketTypesDbService(DataSourceManager ds) {
        super(EventTicketType.class, EventTicketTypeDto.class, ds);
    }

    @Override
    protected EventTicketType mapToEntity(EventTicketTypeDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventTicketTypeDto mapToDto(EventTicketType entity) {
        return entity.toDto();
    }

    @Override
    protected EventTicketTypeDto removeExcludedFields(EventTicketTypeDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
