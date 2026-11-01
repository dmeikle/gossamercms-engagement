package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventTicketDto;
import com.gossamercms.events.models.EventTicket;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventTicketsDbService extends BaseDbService<EventTicket, EventTicketDto> {

    public EventTicketsDbService(DataSourceManager ds) {
        super(EventTicket.class, EventTicketDto.class, ds);
    }

    @Override
    protected EventTicket mapToEntity(EventTicketDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventTicketDto mapToDto(EventTicket entity) {
        return entity.toDto();
    }

    @Override
    protected EventTicketDto removeExcludedFields(EventTicketDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
