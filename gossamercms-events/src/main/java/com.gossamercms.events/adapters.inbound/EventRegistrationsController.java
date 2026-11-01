package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventRegistrationDto;
import com.gossamercms.events.handlers.EventRegistrationsHandler;
import com.gossamercms.events.models.EventRegistration;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-registrations")
public class EventRegistrationsController extends BaseController<EventRegistration, EventRegistrationDto> {

    public EventRegistrationsController(EventRegistrationsHandler handler) {
        super(handler);
    }
}
