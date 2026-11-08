package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventOrganizerAssignmentDto;
import com.gossamercms.events.models.EventOrganizerAssignment;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventOrganizerAssignmentsDbService extends BaseDbService<EventOrganizerAssignment, EventOrganizerAssignmentDto> {

    public EventOrganizerAssignmentsDbService(DataSourceManager ds) {
        super(EventOrganizerAssignment.class, EventOrganizerAssignmentDto.class, ds);
    }

    @Override
    protected EventOrganizerAssignment mapToEntity(EventOrganizerAssignmentDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventOrganizerAssignmentDto mapToDto(EventOrganizerAssignment entity) {
        return entity.toDto();
    }

    @Override
    protected EventOrganizerAssignmentDto removeExcludedFields(EventOrganizerAssignmentDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
