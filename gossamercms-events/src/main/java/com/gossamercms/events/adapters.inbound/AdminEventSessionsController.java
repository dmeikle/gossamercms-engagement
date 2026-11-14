package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.events.dtos.EventSessionDto;
import com.gossamercms.events.dtos.responses.AdminEventSessionListResponse;
import com.gossamercms.events.dtos.responses.AdminEventSessionResponse;
import com.gossamercms.events.dtos.requests.AdminEventSessionRequest;
import com.gossamercms.events.handlers.EventSessionsHandler;
import com.gossamercms.events.models.EventSession;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.controllers.BaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.security.jwt.JwtUser;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/admin/event-sessions")
public class AdminEventSessionsController extends BaseController<EventSession, EventSessionDto, AdminEventSessionRequest, AdminEventSessionResponse, AdminEventSessionListResponse> {

    public AdminEventSessionsController(EventSessionsHandler handler) {
        super(handler, AdminEventSessionListResponse.class);
    }

    @Override
    protected EventSessionDto toDto(AdminEventSessionRequest request) {
        return EventSessionDto.builder()
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

    @PostMapping("/{event}/session")
    public ApiResponse<EventSessionDto> createEventSession(
            @CurrentUser JwtUser jwtUser,
            @PathVariable("event") EventDto event,
            @RequestBody AdminEventSessionRequest request) {

        return ApiResponse.ok(handler.create(
                jwtUser.getUserContextId(),
                EventSessionDto.builder()
                        .description(request.getDescription())
                        .eventId(event.getId())
                        .locationId(request.getLocationId())
                        .title(request.getTitle())
                        .startAt(request.getStartAt())
                        .endAt(request.getEndAt())
                        .timezone(request.getTimezone())
                        .sortOrder(request.getSortOrder())
                        .status(request.getStatus())
                        .build()
                )
        );
    }

    @GetMapping("/{event}/sessions")
    public ApiResponse<ListResultset<AdminEventSessionListResponse>> listEventSession(
            @PathVariable("event") EventDto event,
            @RequestParam(name = "page",defaultValue = "1") int page, @RequestParam(name = "size",defaultValue = "50") int size, @RequestParam Map<String, Object> params) {
        params.remove("page");
        params.remove("size");
        QueryOptions options = QueryOptions.of(page, size, params, Map.of());
        params.put("eventId", event.getId());

        return ApiResponse.ok(handler.getAll(
                QueryOptions.builder()
                        .filters(params)
                        .page(page)
                        .size(size)
                        .build(),
                AdminEventSessionListResponse.class
                )
        );
    }
}
