package com.gossamercms.events.handlers;

import com.gossamercms.events.data.TicketTypesDbService;
import com.gossamercms.events.dtos.TicketTypeDto;
import com.gossamercms.events.models.TicketType;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class TicketTypesHandler extends BaseHandler<TicketType, TicketTypeDto> {
    public TicketTypesHandler(TicketTypesDbService dbService) {
        super(dbService);
    }
}
