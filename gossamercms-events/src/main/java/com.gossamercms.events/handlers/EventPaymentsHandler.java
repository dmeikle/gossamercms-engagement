package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventPaymentsDbService;
import com.gossamercms.events.dtos.EventPaymentDto;
import com.gossamercms.events.models.EventPayment;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventPaymentsHandler extends BaseHandler<EventPayment, EventPaymentDto> {

    public EventPaymentsHandler(EventPaymentsDbService db) {
        super(db);
    }
}
