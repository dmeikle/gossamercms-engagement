package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventWaitlistDto;
import com.gossamercms.events.dtos.responses.AdminEventWaitlistResponse;
import com.gossamercms.events.dtos.requests.AdminEventWaitlistRequest;
import com.gossamercms.events.handlers.EventWaitlistsHandler;
import com.gossamercms.events.models.EventWaitlist;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-waitlists")
public class AdminEventWaitlistsController extends BaseController<EventWaitlist, EventWaitlistDto, AdminEventWaitlistRequest, AdminEventWaitlistResponse, AdminEventWaitlistResponse> {

    public AdminEventWaitlistsController(EventWaitlistsHandler handler) {
        super(handler, AdminEventWaitlistResponse.class);
    }

    @Override
    protected EventWaitlistDto toDto(AdminEventWaitlistRequest request) {
        return EventWaitlistDto.builder()
                .eventId(request.getEventId())
                .ticketTypeId(request.getTicketTypeId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .quantityRequested(request.getQuantityRequested())
                .status(request.getStatus())
                .position(request.getPosition())
                .offeredAt(request.getOfferedAt())
                .expiresAt(request.getExpiresAt())
                .build();
    }

    @Override
    protected AdminEventWaitlistResponse toResponse(EventWaitlistDto dto) {
        return AdminEventWaitlistResponse.builder()
                .id(dto.getId())
                .eventId(dto.getEventId())
                .ticketTypeId(dto.getTicketTypeId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .quantityRequested(dto.getQuantityRequested())
                .status(dto.getStatus())
                .position(dto.getPosition())
                .offeredAt(dto.getOfferedAt())
                .expiresAt(dto.getExpiresAt())
                .build();
    }

}
