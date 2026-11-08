package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventSessionsDbService;
import com.gossamercms.events.dtos.EventSessionDto;
import com.gossamercms.events.models.EventSession;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventSessionsHandler extends BaseHandler<EventSession, EventSessionDto> {

    public EventSessionsHandler(EventSessionsDbService db) {
        super(db);
    }
}
