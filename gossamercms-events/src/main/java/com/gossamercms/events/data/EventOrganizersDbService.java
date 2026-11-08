package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventOrganizerDto;
import com.gossamercms.events.dtos.responses.EventListDto;
import com.gossamercms.events.models.EventOrganizer;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventOrganizersDbService extends BaseDbService<EventOrganizer, EventOrganizerDto> {

    public EventOrganizersDbService(DataSourceManager ds) {
        super(EventOrganizer.class, EventOrganizerDto.class, ds);
    }

    public ListResultset<EventListDto> getEvents(QueryOptions options) {
        String sql = """
                select distinct e.id, e.title from events e
                inner join event_organizer_assignments a on a."eventId" = e.id
                where e."deletedAt" is null
                """;
        String countSql = """
                select count(distinct e.id) from events e
                inner join event_organizer_assignments a on a."eventId" = e.id
                where e."deletedAt" is null
                """;

        return dsManager.get(meta.datasourceKey()).findAllBySql(
                sql,
                countSql,
                options.filters(),
                Map.of("organizerId", "a.organizerId"),
                Collections.emptyMap(),
                options.page(),
                options.size(),
                (rs, rowNum) -> EventListDto.builder()
                        .id(rs.getObject("id", UUID.class))
                        .title(rs.getString("title"))
                        .build()
        );
    }

    @Override
    protected EventOrganizer mapToEntity(EventOrganizerDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventOrganizerDto mapToDto(EventOrganizer entity) {
        return entity.toDto();
    }

    @Override
    protected EventOrganizerDto removeExcludedFields(EventOrganizerDto dto) {
        return dto;
    }
}
