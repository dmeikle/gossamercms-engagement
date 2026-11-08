package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.events.dtos.requests.EventRequest;
import com.gossamercms.events.dtos.requests.EventUpdateRequest;
import com.gossamercms.events.dtos.responses.EventListDto;
import com.gossamercms.events.dtos.responses.EventResponse;
import com.gossamercms.events.handlers.EventsHandler;
import com.gossamercms.events.models.Event;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import com.gossamercms.security.jwt.JwtUser;
import org.springframework.web.bind.annotation.*;
import com.gossamercms.mvc.controllers.TypedBaseController;

import java.util.Map;


@RestController
@RequestMapping("/events")
public class EventsController extends TypedBaseController<
        Event, EventDto, EventRequest, EventResponse> {

    public EventsController(EventsHandler handler) {
        super(handler);
    }

    @Override
    protected EventDto toDto(EventRequest request) {
        return EventDto.builder()
                .title(request.getTitle())
                .slug(request.getSlug())
                .summary(request.getSummary())
                .description(request.getDescription())
                .eventType(request.getEventType())
                .status(request.getStatus())
                .visibility(request.getVisibility())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .timezone(request.getTimezone())
                .bannerImageUrl(request.getBannerImageUrl())
                .thumbnailImageUrl(request.getThumbnailImageUrl())
                .registrationStartAt(request.getRegistrationStartAt())
                .registrationEndAt(request.getRegistrationEndAt())
                .isFeatured(request.getIsFeatured())
                .isPublished(request.getIsPublished())
                .build();
    }

    @Override
    protected EventResponse toResponse(EventDto dto) {
        return EventResponse.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .slug(dto.getSlug())
                .summary(dto.getSummary())
                .description(dto.getDescription())
                .eventType(dto.getEventType())
                .status(dto.getStatus())
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .timezone(dto.getTimezone())
                .bannerImageUrl(dto.getBannerImageUrl())
                .thumbnailImageUrl(dto.getThumbnailImageUrl())
                .registrationStartAt(dto.getRegistrationStartAt())
                .registrationEndAt(dto.getRegistrationEndAt())
                .isFeatured(dto.getIsFeatured())
                .build();
    }


    @GetMapping
    public ApiResponse<ListResultset<EventListDto>> getAllEvents(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam Map<String, Object> params) {
        return ApiResponse.ok(_getAll(jwtUser, page, size, params, EventListDto.class));
    }

    @GetMapping("/{event}")
    protected ApiResponse<EventResponse> getById(@PathVariable("event") EventDto existing) {
        return ApiResponse.ok(toResponse(existing));
    }

}