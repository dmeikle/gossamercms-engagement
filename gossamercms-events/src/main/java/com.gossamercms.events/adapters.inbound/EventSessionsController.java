package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventSessionDto;
import com.gossamercms.events.handlers.EventSessionsHandler;
import com.gossamercms.events.models.EventSession;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-sessions")
public class EventSessionsController extends BaseController<EventSession, EventSessionDto> {

    public EventSessionsController(EventSessionsHandler handler) {
        super(handler);
    }
}
