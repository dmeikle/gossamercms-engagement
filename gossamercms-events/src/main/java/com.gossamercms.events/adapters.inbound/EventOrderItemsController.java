package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventOrderItemDto;
import com.gossamercms.events.handlers.EventOrderItemsHandler;
import com.gossamercms.events.models.EventOrderItem;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-order-items")
public class EventOrderItemsController extends BaseController<EventOrderItem, EventOrderItemDto> {

    public EventOrderItemsController(EventOrderItemsHandler handler) {
        super(handler);
    }
}
