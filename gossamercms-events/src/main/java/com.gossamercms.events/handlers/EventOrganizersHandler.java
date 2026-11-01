package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventOrganizersDbService;
import com.gossamercms.events.dtos.EventOrganizerDto;
import com.gossamercms.events.models.EventOrganizer;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventOrganizersHandler extends BaseHandler<EventOrganizer, EventOrganizerDto> {

    public EventOrganizersHandler(EventOrganizersDbService db) {
        super(db);
    }
}
