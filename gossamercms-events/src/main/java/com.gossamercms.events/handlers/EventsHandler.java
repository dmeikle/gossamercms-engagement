package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventsDbService;
import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.events.models.Event;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventsHandler extends BaseHandler<Event, EventDto> {

    public EventsHandler(EventsDbService db) {
        super(db);
    }
}
