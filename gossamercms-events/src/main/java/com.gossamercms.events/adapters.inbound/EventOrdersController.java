package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventOrderDto;
import com.gossamercms.events.handlers.EventOrdersHandler;
import com.gossamercms.events.models.EventOrder;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-orders")
public class EventOrdersController extends BaseController<EventOrder, EventOrderDto> {

    public EventOrdersController(EventOrdersHandler handler) {
        super(handler);
    }
}
