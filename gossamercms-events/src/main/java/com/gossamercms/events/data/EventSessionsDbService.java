package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.events.dtos.EventSessionDto;
import com.gossamercms.events.dtos.responses.EventDetailWithSessionsResponse;
import com.gossamercms.events.dtos.responses.EventSessionOptionListResponse;
import com.gossamercms.events.models.EventSession;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.*;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventSessionsDbService extends BaseDbService<EventSession, EventSessionDto> {
    private static final JsonMapper JSON_MAPPER = JsonMapper.builder().build();
    private static final TypeReference<List<EventDetailWithSessionsResponse.EventTicketTypeDto>> TICKET_TYPES =
            new TypeReference<>() {};
    private static final TypeReference<List<EventDetailWithSessionsResponse.EventSessionSummaryDto>> SESSIONS =
            new TypeReference<>() {};

    public EventSessionsDbService(DataSourceManager ds) {
        super(EventSession.class, EventSessionDto.class, ds);
    }

    @Override
    protected EventSession mapToEntity(EventSessionDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventSessionDto mapToDto(EventSession entity) {
        return entity.toDto();
    }

    @Override
    protected EventSessionDto removeExcludedFields(EventSessionDto dto) {
        return dto;
    }

    public ListResultset<EventSessionOptionListResponse> getOptionList(QueryOptions options) {
        DataSourceAdapter ds = dsManager.get(meta.datasourceKey());
        String sql = """
                select id, title
                from event_sessions
                /*WHERE_CLAUSE*/
                order by title
                """;

        String countSql = """
                select count(*)
                from event_sessions
                /*WHERE_CLAUSE*/
                """;


        return ds.findAllBySql(sql,
                countSql,
                options.filters(),
                Collections.emptyMap(),
                options.orderBy(),
                options.page(),
                options.size(),
                (rs, rownum) -> EventSessionOptionListResponse.builder()
                        .id(rs.getObject("id", UUID.class))
                        .title(rs.getString("title"))
                        .build()
        );
    }

    public EventDetailWithSessionsResponse getFullDetails(EventDto existing) {
        String sql = """
                WITH ticket_types AS (
                                  SELECT
                                      ett."eventId",
                                      ett."sessionId",
                                      ett.id,
                                      jsonb_build_object(
                                              'id', ett.id,
                                              'name', ett."name",
                                              'status', ett.status,
                                              'currency', ett.currency,
                                              'quantityAvailable', ett."quantityAvailable",
                                              'quantityReserved', ett."quantityReserved",
                                              'price', ett.price,
                                              'quantitySold', ett."quantitySold"
                                      ) AS ticket
                                  FROM event_ticket_types ett
                              )
                              SELECT
                                  ev.id,
                                  ev.title,
                                  ev."startAt",
                                  ev."endAt",
                                  ev."timezone",
                                  COALESCE(event_level_tickets.tickets, '[]'::jsonb) AS "eventTicketTypes",
                                  COALESCE(session_list.sessions, '[]'::jsonb) AS "eventSessions"
                              FROM events ev
                                       LEFT JOIN LATERAL (
                                  SELECT jsonb_agg(tt.ticket ORDER BY tt.id) AS tickets
                                  FROM ticket_types tt
                                  WHERE tt."eventId" = ev.id
                                    AND tt."sessionId" IS NULL
                                  ) event_level_tickets ON true
                                       LEFT JOIN LATERAL (
                                  SELECT jsonb_agg(
                                                 jsonb_build_object(
                                                         'id', es.id,
                                                         'title', es.title,
                                                         'startAt', es."startAt"::timestamptz,
                                                         'endAt', es."endAt"::timestamptz,
                                                         'timezone', es."timezone",
                                                         'sortOrder', es."sortOrder",
                                                         'location', to_jsonb(el),
                                                         'eventTicketTypes', COALESCE(session_tickets.tickets, '[]'::jsonb)
                                                 )
                                                 ORDER BY es."sortOrder", es.id
                                         ) AS sessions
                                  FROM event_sessions es
                                           LEFT JOIN event_locations el ON el.id = es."locationId"
                                           LEFT JOIN LATERAL (
                                      SELECT jsonb_agg(tt.ticket ORDER BY tt.id) AS tickets
                                      FROM ticket_types tt
                                      WHERE tt."eventId" = ev.id
                                        AND tt."sessionId" = es.id
                                      ) session_tickets ON true
                                  WHERE es."eventId" = ev.id
                                  ) session_list ON true
                    WHERE ev.id = :eventId
                """;
        DataSourceAdapter ds = dsManager.get(meta.datasourceKey());
        return ds.getNamedParameterJdbcTemplate().queryForObject(sql,
                Map.of("eventId", existing.getId()),
                (rs, rowNum) -> EventDetailWithSessionsResponse.builder()
                        .id(rs.getObject("id", UUID.class))
                        .title(rs.getString("title"))
                        .startAt(rs.getString("startAt"))
                        .endAt(rs.getString("endAt"))
                        .timezone(rs.getString("timezone"))
                        .eventTicketTypes(JSON_MAPPER.readValue(rs.getString("eventTicketTypes"), TICKET_TYPES))
                        .eventSessions(JSON_MAPPER.readValue(rs.getString("eventSessions"), SESSIONS))
                        .build());
    }
}
