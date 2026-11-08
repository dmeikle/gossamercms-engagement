package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventCheckInDto;
import com.gossamercms.events.dtos.responses.AdminEventCheckInResponse;
import com.gossamercms.events.dtos.requests.AdminEventCheckInRequest;
import com.gossamercms.events.handlers.EventCheckInsHandler;
import com.gossamercms.events.models.EventCheckIn;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-check-ins")
public class AdminEventCheckInsController extends BaseController<EventCheckIn, EventCheckInDto, AdminEventCheckInRequest, AdminEventCheckInResponse, AdminEventCheckInResponse> {

    public AdminEventCheckInsController(EventCheckInsHandler handler) {
        super(handler, AdminEventCheckInResponse.class);
    }

    @Override
    protected EventCheckInDto toDto(AdminEventCheckInRequest request) {
        return EventCheckInDto.builder()
                .ticketId(request.getTicketId())
                .sessionId(request.getSessionId())
                .checkedInBy(request.getCheckedInBy())
                .checkedInAt(request.getCheckedInAt())
                .method(request.getMethod())
                .build();
    }

    @Override
    protected AdminEventCheckInResponse toResponse(EventCheckInDto dto) {
        return AdminEventCheckInResponse.builder()
                .id(dto.getId())
                .ticketId(dto.getTicketId())
                .sessionId(dto.getSessionId())
                .checkedInBy(dto.getCheckedInBy())
                .checkedInAt(dto.getCheckedInAt())
                .method(dto.getMethod())
                .build();
    }

}
