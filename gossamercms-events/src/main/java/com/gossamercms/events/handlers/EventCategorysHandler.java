package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventCategorysDbService;
import com.gossamercms.events.dtos.EventCategoryDto;
import com.gossamercms.events.models.EventCategory;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventCategorysHandler extends BaseHandler<EventCategory, EventCategoryDto> {

    public EventCategorysHandler(EventCategorysDbService db) {
        super(db);
    }
}
