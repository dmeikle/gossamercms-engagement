package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventOrganizerMemberDto;
import com.gossamercms.events.models.EventOrganizerMember;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventOrganizerMembersDbService extends BaseDbService<EventOrganizerMember, EventOrganizerMemberDto> {

    public EventOrganizerMembersDbService(DataSourceManager ds) {
        super(EventOrganizerMember.class, EventOrganizerMemberDto.class, ds);
    }

    @Override
    protected EventOrganizerMember mapToEntity(EventOrganizerMemberDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventOrganizerMemberDto mapToDto(EventOrganizerMember entity) {
        return entity.toDto();
    }

    @Override
    protected EventOrganizerMemberDto removeExcludedFields(EventOrganizerMemberDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
