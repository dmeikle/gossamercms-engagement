package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventTicketsDbService;
import com.gossamercms.events.dtos.EventTicketDto;
import com.gossamercms.events.models.EventTicket;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventTicketsHandler extends BaseHandler<EventTicket, EventTicketDto> {

    public EventTicketsHandler(EventTicketsDbService db) {
        super(db);
    }
}
