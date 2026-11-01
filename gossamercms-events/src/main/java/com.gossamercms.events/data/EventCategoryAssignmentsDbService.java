package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventCategoryAssignmentDto;
import com.gossamercms.events.models.EventCategoryAssignment;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventCategoryAssignmentsDbService extends BaseDbService<EventCategoryAssignment, EventCategoryAssignmentDto> {

    public EventCategoryAssignmentsDbService(DataSourceManager ds) {
        super(EventCategoryAssignment.class, EventCategoryAssignmentDto.class, ds);
    }

    @Override
    protected EventCategoryAssignment mapToEntity(EventCategoryAssignmentDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventCategoryAssignmentDto mapToDto(EventCategoryAssignment entity) {
        return entity.toDto();
    }

    @Override
    protected EventCategoryAssignmentDto removeExcludedFields(EventCategoryAssignmentDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
