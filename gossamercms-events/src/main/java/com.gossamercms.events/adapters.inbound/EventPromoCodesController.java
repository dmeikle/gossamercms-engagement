package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventPromoCodeDto;
import com.gossamercms.events.handlers.EventPromoCodesHandler;
import com.gossamercms.events.models.EventPromoCode;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-promo-codes")
public class EventPromoCodesController extends BaseController<EventPromoCode, EventPromoCodeDto> {

    public EventPromoCodesController(EventPromoCodesHandler handler) {
        super(handler);
    }
}
