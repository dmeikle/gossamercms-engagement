package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventOrganizerMemberDto;
import com.gossamercms.events.handlers.EventOrganizerMembersHandler;
import com.gossamercms.events.models.EventOrganizerMember;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-organizer-members")
public class EventOrganizerMembersController extends BaseController<EventOrganizerMember, EventOrganizerMemberDto> {

    public EventOrganizerMembersController(EventOrganizerMembersHandler handler) {
        super(handler);
    }
}
