package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventCategoryDto;
import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.events.dtos.requests.EventCategoryRequest;
import com.gossamercms.events.dtos.requests.EventRequest;
import com.gossamercms.events.dtos.responses.EventCategoryResponse;
import com.gossamercms.events.dtos.responses.EventListDto;
import com.gossamercms.events.dtos.responses.EventResponse;
import com.gossamercms.events.handlers.EventCategorysHandler;
import com.gossamercms.events.models.EventCategory;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.controllers.TypedBaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import com.gossamercms.security.jwt.JwtUser;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/event-categories")
public class EventCategoriesController extends TypedBaseController<EventCategory, EventCategoryDto, EventCategoryRequest, EventCategoryResponse> {

    public EventCategoriesController(EventCategorysHandler handler) {
        super(handler);
    }


    @Override
    protected EventCategoryDto toDto(EventCategoryRequest request) {
        return EventCategoryDto.builder()
                .name(request.getName())
                .description(request.getDescription())
                .parentId(request.getParentId())
                .isActive(request.getIsActive())
                .slug(request.getSlug())
                .sortOrder(request.getSortOrder())
                .build();
    }

    @Override
    protected EventCategoryResponse toResponse(EventCategoryDto dto) {
        return EventCategoryResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .parentId(dto.getParentId())
                .isActive(dto.getIsActive())
                .slug(dto.getSlug())
                .sortOrder(dto.getSortOrder())
                .build();
    }

    //TODO: integrate with EventController to get events by category
    @GetMapping("/{eventCategory}/events")
    public ApiResponse<ListResultset<EventListDto>> getAllEvents(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam Map<String, Object> params) {
        return ApiResponse.ok(_getAll(jwtUser, page, size, params, EventListDto.class));
    }

    @GetMapping
    public ApiResponse<ListResultset<EventCategoryResponse>> getAllEventCategories(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam Map<String, Object> params) {
        return ApiResponse.ok(_getAll(jwtUser, page, size, params, EventCategoryResponse.class));
    }

    @GetMapping("/{eventCategory}")
    protected ApiResponse<EventCategoryResponse> getById(@PathVariable("eventCategory") EventCategoryDto existing) {
        return ApiResponse.ok(toResponse(existing));
    }

}
