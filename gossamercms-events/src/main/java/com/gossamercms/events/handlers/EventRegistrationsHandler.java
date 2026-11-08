package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventRegistrationsDbService;
import com.gossamercms.events.dtos.EventRegistrationDto;
import com.gossamercms.events.models.EventRegistration;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventRegistrationsHandler extends BaseHandler<EventRegistration, EventRegistrationDto> {

    public EventRegistrationsHandler(EventRegistrationsDbService db) {
        super(db);
    }
}
