package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventOrdersDbService;
import com.gossamercms.events.dtos.EventOrderDto;
import com.gossamercms.events.models.EventOrder;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventOrdersHandler extends BaseHandler<EventOrder, EventOrderDto> {

    public EventOrdersHandler(EventOrdersDbService db) {
        super(db);
    }
}
