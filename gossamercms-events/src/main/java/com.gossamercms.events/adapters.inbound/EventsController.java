package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.events.handlers.EventsHandler;
import com.gossamercms.events.models.Event;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/events")
public class EventsController extends BaseController<Event, EventDto> {

    public EventsController(EventsHandler handler) {
        super(handler);
    }
}
