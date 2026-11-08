package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventOrganizerAssignmentsDbService;
import com.gossamercms.events.dtos.EventOrganizerAssignmentDto;
import com.gossamercms.events.models.EventOrganizerAssignment;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventOrganizerAssignmentsHandler extends BaseHandler<EventOrganizerAssignment, EventOrganizerAssignmentDto> {

    public EventOrganizerAssignmentsHandler(EventOrganizerAssignmentsDbService db) {
        super(db);
    }
}
