package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventPaymentDto;
import com.gossamercms.events.handlers.EventPaymentsHandler;
import com.gossamercms.events.models.EventPayment;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-payments")
public class EventPaymentsController extends BaseController<EventPayment, EventPaymentDto> {

    public EventPaymentsController(EventPaymentsHandler handler) {
        super(handler);
    }
}
