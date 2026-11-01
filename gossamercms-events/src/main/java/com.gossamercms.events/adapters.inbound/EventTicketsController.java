package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventTicketDto;
import com.gossamercms.events.handlers.EventTicketsHandler;
import com.gossamercms.events.models.EventTicket;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-tickets")
public class EventTicketsController extends BaseController<EventTicket, EventTicketDto> {

    public EventTicketsController(EventTicketsHandler handler) {
        super(handler);
    }
}
