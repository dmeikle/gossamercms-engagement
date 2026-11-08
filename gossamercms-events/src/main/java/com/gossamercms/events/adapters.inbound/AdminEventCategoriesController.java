package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventCategoryDto;
import com.gossamercms.events.dtos.responses.EventCategoryResponse;
import com.gossamercms.events.dtos.requests.EventCategoryRequest;
import com.gossamercms.events.handlers.EventCategorysHandler;
import com.gossamercms.events.models.EventCategory;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-categories")
public class AdminEventCategoriesController extends BaseController<EventCategory, EventCategoryDto, EventCategoryRequest, EventCategoryResponse, EventCategoryResponse> {

    public AdminEventCategoriesController(EventCategorysHandler handler) {
        super(handler, EventCategoryResponse.class);
    }

    @Override
    protected EventCategoryDto toDto(EventCategoryRequest request) {
        return EventCategoryDto.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .parentId(request.getParentId())
                .sortOrder(request.getSortOrder())
                .isActive(request.getIsActive())
                .build();
    }

    @Override
    protected EventCategoryResponse toResponse(EventCategoryDto dto) {
        return EventCategoryResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .slug(dto.getSlug())
                .description(dto.getDescription())
                .parentId(dto.getParentId())
                .sortOrder(dto.getSortOrder())
                .isActive(dto.getIsActive())
                .build();
    }

}
