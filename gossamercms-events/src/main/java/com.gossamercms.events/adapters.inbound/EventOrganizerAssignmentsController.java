package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventOrganizerAssignmentDto;
import com.gossamercms.events.handlers.EventOrganizerAssignmentsHandler;
import com.gossamercms.events.models.EventOrganizerAssignment;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-organizer-assignments")
public class EventOrganizerAssignmentsController extends BaseController<EventOrganizerAssignment, EventOrganizerAssignmentDto> {

    public EventOrganizerAssignmentsController(EventOrganizerAssignmentsHandler handler) {
        super(handler);
    }
}
