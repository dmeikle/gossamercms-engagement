package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventLocationsDbService;
import com.gossamercms.events.dtos.EventLocationDto;
import com.gossamercms.events.models.EventLocation;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventLocationsHandler extends BaseHandler<EventLocation, EventLocationDto> {

    public EventLocationsHandler(EventLocationsDbService db) {
        super(db);
    }
}
