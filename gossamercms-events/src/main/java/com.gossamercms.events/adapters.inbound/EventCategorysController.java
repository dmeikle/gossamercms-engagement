package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventCategoryDto;
import com.gossamercms.events.handlers.EventCategorysHandler;
import com.gossamercms.events.models.EventCategory;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-categories")
public class EventCategorysController extends BaseController<EventCategory, EventCategoryDto> {

    public EventCategorysController(EventCategorysHandler handler) {
        super(handler);
    }
}
