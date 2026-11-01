package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventWaitlistDto;
import com.gossamercms.events.handlers.EventWaitlistsHandler;
import com.gossamercms.events.models.EventWaitlist;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-waitlists")
public class EventWaitlistsController extends BaseController<EventWaitlist, EventWaitlistDto> {

    public EventWaitlistsController(EventWaitlistsHandler handler) {
        super(handler);
    }
}
