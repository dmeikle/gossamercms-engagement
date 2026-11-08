package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventWaitlistsDbService;
import com.gossamercms.events.dtos.EventWaitlistDto;
import com.gossamercms.events.models.EventWaitlist;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventWaitlistsHandler extends BaseHandler<EventWaitlist, EventWaitlistDto> {

    public EventWaitlistsHandler(EventWaitlistsDbService db) {
        super(db);
    }
}
