package com.gossamercms.events.adapters.inbound;

import com.gossamercms.events.dtos.EventWaitlistDto;
import com.gossamercms.events.dtos.requests.EventWaitlistRequest;
import com.gossamercms.events.dtos.responses.EventWaitlistResponse;
import com.gossamercms.events.handlers.EventWaitlistsHandler;
import com.gossamercms.events.models.EventWaitlist;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.controllers.TypedBaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/event-waitlists")
public class EventWaitlistsController extends TypedBaseController<
        EventWaitlist, EventWaitlistDto, EventWaitlistRequest, EventWaitlistResponse, EventWaitlistResponse> {

    public EventWaitlistsController(EventWaitlistsHandler handler) {
        super(handler, EventWaitlistResponse.class);
    }

    @Override
    protected EventWaitlistDto toDto(EventWaitlistRequest request) {
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
    protected EventWaitlistResponse toResponse(EventWaitlistDto dto) {
        return EventWaitlistResponse.builder()
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


    @GetMapping
    public ApiResponse<ListResultset<EventWaitlistResponse>> getAll(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam Map<String, Object> params) {
        return ApiResponse.ok(_getAll(jwtUser, page, size, params));
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<EventWaitlistResponse> getById(@PathVariable("id") EventWaitlistDto existing) {
        return super.getById(existing);
    }
}
