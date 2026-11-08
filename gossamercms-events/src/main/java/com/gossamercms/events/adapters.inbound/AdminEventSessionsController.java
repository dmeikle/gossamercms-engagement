package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventSessionDto;
import com.gossamercms.events.dtos.responses.AdminEventSessionResponse;
import com.gossamercms.events.dtos.requests.AdminEventSessionRequest;
import com.gossamercms.events.handlers.EventSessionsHandler;
import com.gossamercms.events.models.EventSession;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-sessions")
public class AdminEventSessionsController extends BaseController<EventSession, EventSessionDto, AdminEventSessionRequest, AdminEventSessionResponse, AdminEventSessionResponse> {

    public AdminEventSessionsController(EventSessionsHandler handler) {
        super(handler, AdminEventSessionResponse.class);
    }

    @Override
    protected EventSessionDto toDto(AdminEventSessionRequest request) {
        return EventSessionDto.builder()
                .eventId(request.getEventId())
                .locationId(request.getLocationId())
                .title(request.getTitle())
                .description(request.getDescription())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .timezone(request.getTimezone())
                .sortOrder(request.getSortOrder())
                .status(request.getStatus())
                .build();
    }

    @Override
    protected AdminEventSessionResponse toResponse(EventSessionDto dto) {
        return AdminEventSessionResponse.builder()
                .id(dto.getId())
                .eventId(dto.getEventId())
                .locationId(dto.getLocationId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .timezone(dto.getTimezone())
                .sortOrder(dto.getSortOrder())
                .status(dto.getStatus())
                .build();
    }

}
