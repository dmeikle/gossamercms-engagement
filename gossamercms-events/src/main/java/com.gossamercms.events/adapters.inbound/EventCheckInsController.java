package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventCheckInDto;
import com.gossamercms.events.handlers.EventCheckInsHandler;
import com.gossamercms.events.models.EventCheckIn;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-check-ins")
public class EventCheckInsController extends BaseController<EventCheckIn, EventCheckInDto> {

    public EventCheckInsController(EventCheckInsHandler handler) {
        super(handler);
    }
}
