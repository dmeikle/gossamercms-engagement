package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventCategoryDto;
import com.gossamercms.events.models.EventCategory;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventCategorysDbService extends BaseDbService<EventCategory, EventCategoryDto> {

    public EventCategorysDbService(DataSourceManager ds) {
        super(EventCategory.class, EventCategoryDto.class, ds);
    }

    @Override
    protected EventCategory mapToEntity(EventCategoryDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventCategoryDto mapToDto(EventCategory entity) {
        return entity.toDto();
    }

    @Override
    protected EventCategoryDto removeExcludedFields(EventCategoryDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
