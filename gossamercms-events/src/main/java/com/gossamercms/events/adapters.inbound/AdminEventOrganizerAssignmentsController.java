package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventOrganizerAssignmentDto;
import com.gossamercms.events.dtos.responses.AdminEventOrganizerAssignmentResponse;
import com.gossamercms.events.dtos.requests.AdminEventOrganizerAssignmentRequest;
import com.gossamercms.events.handlers.EventOrganizerAssignmentsHandler;
import com.gossamercms.events.models.EventOrganizerAssignment;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-organizer-assignments")
public class AdminEventOrganizerAssignmentsController extends BaseController<EventOrganizerAssignment, EventOrganizerAssignmentDto, AdminEventOrganizerAssignmentRequest, AdminEventOrganizerAssignmentResponse, AdminEventOrganizerAssignmentResponse> {

    public AdminEventOrganizerAssignmentsController(EventOrganizerAssignmentsHandler handler) {
        super(handler, AdminEventOrganizerAssignmentResponse.class);
    }

    @Override
    protected EventOrganizerAssignmentDto toDto(AdminEventOrganizerAssignmentRequest request) {
        return EventOrganizerAssignmentDto.builder()
                .eventId(request.getEventId())
                .organizerId(request.getOrganizerId())
                .role(request.getRole())
                .isPrimary(request.getIsPrimary())
                .build();
    }

    @Override
    protected AdminEventOrganizerAssignmentResponse toResponse(EventOrganizerAssignmentDto dto) {
        return AdminEventOrganizerAssignmentResponse.builder()
                .id(dto.getId())
                .eventId(dto.getEventId())
                .organizerId(dto.getOrganizerId())
                .role(dto.getRole())
                .isPrimary(dto.getIsPrimary())
                .build();
    }

}
