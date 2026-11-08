package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventCheckInsDbService;
import com.gossamercms.events.dtos.EventCheckInDto;
import com.gossamercms.events.models.EventCheckIn;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventCheckInsHandler extends BaseHandler<EventCheckIn, EventCheckInDto> {

    public EventCheckInsHandler(EventCheckInsDbService db) {
        super(db);
    }
}
