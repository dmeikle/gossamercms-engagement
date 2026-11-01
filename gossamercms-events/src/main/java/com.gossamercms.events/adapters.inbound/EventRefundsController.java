package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventRefundDto;
import com.gossamercms.events.handlers.EventRefundsHandler;
import com.gossamercms.events.models.EventRefund;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-refunds")
public class EventRefundsController extends BaseController<EventRefund, EventRefundDto> {

    public EventRefundsController(EventRefundsHandler handler) {
        super(handler);
    }
}
