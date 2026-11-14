package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.events.dtos.responses.AdminEventResponse;
import com.gossamercms.events.dtos.requests.EventRequest;
import com.gossamercms.events.handlers.EventsHandler;
import com.gossamercms.events.models.Event;
import com.gossamercms.events.services.EventsService;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.controllers.BaseController;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import com.gossamercms.security.jwt.JwtUser;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/events")
public class AdminEventsController extends BaseController<Event, EventDto, EventRequest, AdminEventResponse, AdminEventResponse> {

    private final EventsService eventsService;

    public AdminEventsController(EventsHandler handler, EventsService eventsService) {
        super(handler, AdminEventResponse.class);
        this.eventsService = eventsService;
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
    protected AdminEventResponse toResponse(EventDto dto) {
        return AdminEventResponse.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .slug(dto.getSlug())
                .summary(dto.getSummary())
                .description(dto.getDescription())
                .eventType(dto.getEventType())
                .status(dto.getStatus())
                .visibility(dto.getVisibility())
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .timezone(dto.getTimezone())
                .bannerImageUrl(dto.getBannerImageUrl())
                .thumbnailImageUrl(dto.getThumbnailImageUrl())
                .registrationStartAt(dto.getRegistrationStartAt())
                .registrationEndAt(dto.getRegistrationEndAt())
                .isFeatured(dto.getIsFeatured())
                .isPublished(dto.getIsPublished())
                .build();
    }

    /**********************************
     *  Event Methods
     **********************************/

    @PostMapping("/{event}/publish")
    protected ApiResponse<EventDto> publishEvent(
            @CurrentUser JwtUser jwtUser,
            @PathVariable("event") EventDto existing
    ) {
        return ApiResponse.ok(handler.updateById(
                jwtUser.getUserId(),
                existing.getId(),
                EventDto.builder()
                        .isPublished(true)
                        .build()));
    }

    @PostMapping("/{event}/cancel")
    protected ApiResponse<EventDto> cancelEvent(
            @CurrentUser JwtUser jwtUser,
            @PathVariable("event") EventDto existing
    ) {
        return ApiResponse.ok(handler.updateById(
                jwtUser.getUserId(),
                existing.getId(),
                EventDto.builder()
                        .isPublished(false)
                        .build()));
    }

    @PutMapping("/{event}/details")
    public ApiResponse<AdminEventResponse> updateDetails(
            @CurrentUser CurrentJwtUser jwtUser,
            @PathVariable("event") EventDto existing,
            @RequestBody EventRequest request) {

        return ApiResponse.ok(eventsService.update(jwtUser, existing, request));
    }
    @GetMapping("/{event}/details")
    public ApiResponse<AdminEventResponse> getDetails(
            @CurrentUser CurrentJwtUser jwtUser,
            @PathVariable("event") EventDto existing
            ) {

        return ApiResponse.ok(eventsService.getDetails(existing));
    }

    @Override
    @PostMapping()
    public ApiResponse<AdminEventResponse> create(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestBody EventRequest request) {

        return ApiResponse.ok(eventsService.create(jwtUser, request));
    }
}
