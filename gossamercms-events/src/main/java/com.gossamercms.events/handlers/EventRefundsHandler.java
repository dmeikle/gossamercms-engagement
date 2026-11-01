package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventRefundsDbService;
import com.gossamercms.events.dtos.EventRefundDto;
import com.gossamercms.events.models.EventRefund;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventRefundsHandler extends BaseHandler<EventRefund, EventRefundDto> {

    public EventRefundsHandler(EventRefundsDbService db) {
        super(db);
    }
}
