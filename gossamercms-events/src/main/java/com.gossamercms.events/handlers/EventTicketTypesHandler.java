package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventTicketTypesDbService;
import com.gossamercms.events.dtos.EventTicketTypeDto;
import com.gossamercms.events.models.EventTicketType;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventTicketTypesHandler extends BaseHandler<EventTicketType, EventTicketTypeDto> {

    public EventTicketTypesHandler(EventTicketTypesDbService db) {
        super(db);
    }
}
