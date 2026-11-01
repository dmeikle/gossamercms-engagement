package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventPromoCodesDbService;
import com.gossamercms.events.dtos.EventPromoCodeDto;
import com.gossamercms.events.models.EventPromoCode;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventPromoCodesHandler extends BaseHandler<EventPromoCode, EventPromoCodeDto> {

    public EventPromoCodesHandler(EventPromoCodesDbService db) {
        super(db);
    }
}
