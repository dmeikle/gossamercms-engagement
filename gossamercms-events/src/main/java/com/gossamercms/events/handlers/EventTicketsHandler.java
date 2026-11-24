package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventTicketsDbService;
import com.gossamercms.events.dtos.EventTicketDto;
import com.gossamercms.events.dtos.responses.EventTicketDetailResponse;
import com.gossamercms.events.dtos.responses.EventTicketListResponse;
import com.gossamercms.events.models.EventTicket;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.mvc.handlers.BaseHandler;
import com.gossamercms.mvc.http.ApiResponse;

import java.util.UUID;

@ModuleHandler
public class EventTicketsHandler extends BaseHandler<EventTicket, EventTicketDto> {

    public EventTicketsHandler(EventTicketsDbService db) {
        super(db);
    }

    public ListResultset<EventTicketListResponse> listByUserContext(QueryOptions options) {
        return ((EventTicketsDbService) db).listByUserContext(options);
    }

    public ListResultset<EventTicketDetailResponse> getTicketDetails(UUID eventTicketId) {
        return ((EventTicketsDbService) db).getTicketDetails(eventTicketId);
    }
}
