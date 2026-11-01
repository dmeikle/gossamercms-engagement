package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventCategoryAssignmentDto;
import com.gossamercms.events.handlers.EventCategoryAssignmentsHandler;
import com.gossamercms.events.models.EventCategoryAssignment;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-category-assignments")
public class EventCategoryAssignmentsController extends BaseController<EventCategoryAssignment, EventCategoryAssignmentDto> {

    public EventCategoryAssignmentsController(EventCategoryAssignmentsHandler handler) {
        super(handler);
    }
}
