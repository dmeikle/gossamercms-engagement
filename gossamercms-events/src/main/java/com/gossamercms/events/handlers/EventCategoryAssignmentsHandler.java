package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventCategoryAssignmentsDbService;
import com.gossamercms.events.dtos.EventCategoryAssignmentDto;
import com.gossamercms.events.models.EventCategoryAssignment;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventCategoryAssignmentsHandler extends BaseHandler<EventCategoryAssignment, EventCategoryAssignmentDto> {

    public EventCategoryAssignmentsHandler(EventCategoryAssignmentsDbService db) {
        super(db);
    }
}
