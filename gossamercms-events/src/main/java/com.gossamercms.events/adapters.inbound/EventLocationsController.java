package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventLocationDto;
import com.gossamercms.events.handlers.EventLocationsHandler;
import com.gossamercms.events.models.EventLocation;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-locations")
public class EventLocationsController extends BaseController<EventLocation, EventLocationDto> {

    public EventLocationsController(EventLocationsHandler handler) {
        super(handler);
    }
}
