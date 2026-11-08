package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventOrganizerMemberDto;
import com.gossamercms.events.dtos.responses.AdminEventOrganizerMemberResponse;
import com.gossamercms.events.dtos.requests.AdminEventOrganizerMemberRequest;
import com.gossamercms.events.handlers.EventOrganizerMembersHandler;
import com.gossamercms.events.models.EventOrganizerMember;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-organizer-members")
public class AdminEventOrganizerMembersController extends BaseController<EventOrganizerMember, EventOrganizerMemberDto, AdminEventOrganizerMemberRequest, AdminEventOrganizerMemberResponse, AdminEventOrganizerMemberResponse> {

    public AdminEventOrganizerMembersController(EventOrganizerMembersHandler handler) {
        super(handler, AdminEventOrganizerMemberResponse.class);
    }

    @Override
    protected EventOrganizerMemberDto toDto(AdminEventOrganizerMemberRequest request) {
        return EventOrganizerMemberDto.builder()
                .organizerId(request.getOrganizerId())
                .userId(request.getUserId())
                .role(request.getRole())
                .status(request.getStatus())
                .invitedAt(request.getInvitedAt())
                .joinedAt(request.getJoinedAt())
                .build();
    }

    @Override
    protected AdminEventOrganizerMemberResponse toResponse(EventOrganizerMemberDto dto) {
        return AdminEventOrganizerMemberResponse.builder()
                .id(dto.getId())
                .organizerId(dto.getOrganizerId())
                .userId(dto.getUserId())
                .role(dto.getRole())
                .status(dto.getStatus())
                .invitedAt(dto.getInvitedAt())
                .joinedAt(dto.getJoinedAt())
                .build();
    }

}
