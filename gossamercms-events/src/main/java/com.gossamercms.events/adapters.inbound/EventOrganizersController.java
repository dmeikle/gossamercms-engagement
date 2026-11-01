package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventOrganizerDto;
import com.gossamercms.events.handlers.EventOrganizersHandler;
import com.gossamercms.events.models.EventOrganizer;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-organizers")
public class EventOrganizersController extends BaseController<EventOrganizer, EventOrganizerDto> {

    public EventOrganizersController(EventOrganizersHandler handler) {
        super(handler);
    }
}
