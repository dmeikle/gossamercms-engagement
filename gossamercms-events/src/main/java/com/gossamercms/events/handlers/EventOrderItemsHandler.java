package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventOrderItemsDbService;
import com.gossamercms.events.dtos.EventOrderItemDto;
import com.gossamercms.events.models.EventOrderItem;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventOrderItemsHandler extends BaseHandler<EventOrderItem, EventOrderItemDto> {

    public EventOrderItemsHandler(EventOrderItemsDbService db) {
        super(db);
    }
}
