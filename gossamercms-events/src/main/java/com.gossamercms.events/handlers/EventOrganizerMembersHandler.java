package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventOrganizerMembersDbService;
import com.gossamercms.events.dtos.EventOrganizerMemberDto;
import com.gossamercms.events.models.EventOrganizerMember;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventOrganizerMembersHandler extends BaseHandler<EventOrganizerMember, EventOrganizerMemberDto> {

    public EventOrganizerMembersHandler(EventOrganizerMembersDbService db) {
        super(db);
    }
}
