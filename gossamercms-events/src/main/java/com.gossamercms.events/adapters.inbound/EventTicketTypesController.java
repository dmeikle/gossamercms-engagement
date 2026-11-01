package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventTicketTypeDto;
import com.gossamercms.events.handlers.EventTicketTypesHandler;
import com.gossamercms.events.models.EventTicketType;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-ticket-types")
public class EventTicketTypesController extends BaseController<EventTicketType, EventTicketTypeDto> {

    public EventTicketTypesController(EventTicketTypesHandler handler) {
        super(handler);
    }
}
