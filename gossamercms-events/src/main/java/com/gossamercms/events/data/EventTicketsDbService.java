package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventTicketDto;
import com.gossamercms.events.dtos.responses.EventTicketDetailResponse;
import com.gossamercms.events.dtos.responses.EventTicketListResponse;
import com.gossamercms.events.models.EventTicket;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.*;
import com.gossamercms.mvc.http.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@ModuleDbService
public class EventTicketsDbService extends BaseDbService<EventTicket, EventTicketDto> {
    private static final JsonMapper JSON_MAPPER = JsonMapper.builder().build();
    private static final TypeReference<List<EventTicketListResponse.Ticket>> TICKETS = new TypeReference<>() {};
    private static final TypeReference<List<EventTicketListResponse.Session>> SESSIONS = new TypeReference<>() {};

    public EventTicketsDbService(DataSourceManager ds) {
        super(EventTicket.class, EventTicketDto.class, ds);
    }

    @Override
    protected EventTicket mapToEntity(EventTicketDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventTicketDto mapToDto(EventTicket entity) {
        return entity.toDto();
    }

    @Override
    protected EventTicketDto removeExcludedFields(EventTicketDto dto) {
        return dto;
    }

    public long getNextPurchaseSequenceValue() {
        DataSourceAdapter ds = dsManager.get(meta.datasourceKey());
        String sql = """
        SELECT nextval('ecommerce_ticket_number_seq')
        """;

        return ds.executeSql(sql, Long.class);
    }

    public ListResultset<EventTicketListResponse> listByUserContext(QueryOptions options) {
        if (!(options.filters().get("userContextId") instanceof UUID)) {
            throw new IllegalArgumentException("A user context ID is required to list tickets");
        }

        Set<String> supportedFilters = Set.of("userContextId", "status", "eventId", "ticketTypeId");
        if (!supportedFilters.containsAll(options.filters().keySet()) || !options.queryFilters().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported ticket list filter");
        }
        Map<String, Object> params = new HashMap<>(options.filters());
        for (String key : List.of("eventId", "ticketTypeId")) {
            Object value = params.get(key);
            if (value instanceof String text) {
                try {
                    params.put(key, UUID.fromString(text));
                } catch (IllegalArgumentException ex) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid " + key, ex);
                }
            } else if (value != null && !(value instanceof UUID)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid " + key);
            }
        }
        int page = Math.max(1, options.page());
        int size = Math.max(1, options.size());
        params.put("limit", size);
        params.put("offset", ((long) page - 1) * size);
        String filters = (params.containsKey("status") ? " AND et.status = :status" : "")
                + (params.containsKey("eventId") ? " AND er.\"eventId\" = :eventId" : "")
                + (params.containsKey("ticketTypeId") ? " AND et.\"ticketTypeId\" = :ticketTypeId" : "");

        String purchasedTickets = """
                WITH purchased_tickets AS (
                   SELECT
                       er."eventId",
                       ett."sessionId",
                       et.id AS "ticketId",
                       et."issuedAt",
                       jsonb_build_object(
                               'ticketId', et.id,
                               'orderNumber', eo."orderNumber",
                               'total', eo.total,
                               'currency', eo.currency,
                               'paidAt', eo."paidAt"::timestamptz,
                               'eventTicketTypeName', ett.name,
                               'ticketTypeName', tt.name,
                               'registrationId', er.id,
                               'registeredAt', er."registeredAt"::timestamptz,
                               'cancelledAt', er."cancelledAt"::timestamptz,
                               'eventId', er."eventId",
                               'ticketTypeId', et."ticketTypeId",
                               'ticketNumber', et."ticketNumber",
                               'qrTokenHash', et."qrTokenHash",
                               'status', et.status,
                               'issuedAt', et."issuedAt"::timestamptz
                       ) AS ticket
                   FROM event_orders eo
                            JOIN event_registrations er ON er."orderId" = eo.id
                            JOIN event_tickets et ON et."registrationId" = er.id
                            JOIN event_ticket_types ett ON ett.id = et."ticketTypeId"
                            JOIN ticket_types tt ON tt.id = ett."ticketTypeId"
                   WHERE er."userContextId" = :userContextId and er."status" = 'REGISTERED'
                """ + filters + """

               )
                """;
        String sql = purchasedTickets + """
               SELECT
                   e.id AS "eventId",
                   e.title,
                   e.slug,
                   e.summary,
                   e."eventType",
                   e."startAt",
                   e."endAt",
                   e.timezone,
                   e."thumbnailImageUrl",
                   e."isFeatured",
                   COALESCE((
                                SELECT jsonb_agg(pt.ticket ORDER BY pt."issuedAt", pt."ticketId")
                                FROM purchased_tickets pt
                                WHERE pt."eventId" = e.id
                                  AND pt."sessionId" IS NULL
                            ), '[]'::jsonb) AS tickets,
                   COALESCE((
                                SELECT jsonb_agg(
                                               jsonb_build_object(
                                                       'id', es.id,
                                                       'title', es.title,
                                                       'startAt', es."startAt"::timestamptz,
                                                       'endAt', es."endAt"::timestamptz,
                                                       'timezone', es.timezone,
                                                       'sortOrder', es."sortOrder",
                                                       'location', to_jsonb(el),
                                                       'tickets', session_tickets.tickets
                                               )
                                               ORDER BY es."sortOrder", es.id
                                       )
                                FROM event_sessions es
                                         LEFT JOIN event_locations el ON el.id = es."locationId"
                                         JOIN LATERAL (
                                    SELECT jsonb_agg(
                                                   pt.ticket ORDER BY pt."issuedAt", pt."ticketId"
                                           ) AS tickets
                                    FROM purchased_tickets pt
                                    WHERE pt."eventId" = e.id
                                      AND pt."sessionId" = es.id
                                    ) session_tickets ON session_tickets.tickets IS NOT NULL
                                WHERE es."eventId" = e.id
                            ), '[]'::jsonb) AS sessions
               FROM events e
               WHERE EXISTS (
                   SELECT 1
                   FROM purchased_tickets pt
                   WHERE pt."eventId" = e.id
               )
               ORDER BY e."startAt", e.id
               LIMIT :limit OFFSET :offset
                """;
        String countSql = purchasedTickets + """
                SELECT count(*) FROM events e
                WHERE EXISTS (SELECT 1 FROM purchased_tickets pt WHERE pt."eventId" = e.id)
                """;

        var jdbc = dsManager.get(meta.datasourceKey()).getNamedParameterJdbcTemplate();
        List<EventTicketListResponse> results = jdbc.query(sql, params,
                (rs, rowNum) -> EventTicketListResponse.builder()
                        .eventId(rs.getObject("eventId", UUID.class))
                        .title(rs.getString("title"))
                        .slug(rs.getString("slug"))
                        .summary(rs.getString("summary"))
                        .eventType(rs.getString("eventType"))
                        .startAt(toInstant(rs.getTimestamp("startAt")))
                        .endAt(toInstant(rs.getTimestamp("endAt")))
                        .timezone(rs.getString("timezone"))
                        .thumbnailImageUrl(rs.getString("thumbnailImageUrl"))
                        .isFeatured(rs.getObject("isFeatured", Boolean.class))
                        .tickets(JSON_MAPPER.readValue(rs.getString("tickets"), TICKETS))
                        .sessions(JSON_MAPPER.readValue(rs.getString("sessions"), SESSIONS))
                        .build());
        Long count = jdbc.queryForObject(countSql, params, Long.class);
        if (count == null) {
            throw new IllegalStateException("Ticket event count query returned null");
        }
        return ListResultset.of(results, page, size, count);
    }

    public ListResultset<EventTicketDetailResponse> getTicketDetails(UUID eventTicketId) {
        String sql = """
                select et.id as "ticketId", et."ticketTypeId", et."ticketNumber", et."status",
                       eo.id as "orderId", eo."orderNumber", eo."status" as "orderStatus",
                       ett.name as "ticketTypeName", ett."price",
                       es.title as "sessionTitle", es."description", es."startAt", es."endAt",
                       e.title as "eventTitle", e."description" as "eventDescription", e.summary as "eventSummary", e."startAt" as "eventStartAt", e."endAt" as "eventEndAt",
                       er."attendeeEmail", er."attendeeFirstName", er."attendeeLastName"
                      -- tt.name as "ticketTypeName"
                from event_tickets et
                inner join event_registrations er on er."id" = et."registrationId"
                inner join event_orders eo on eo."id" = er."orderId"
                inner join event_order_items eoi on eoi."orderId" = eo.id
                inner join event_ticket_types ett on ett.id = et."ticketTypeId"
                  --  inner join ticket_types tt on tt.id = et."ticketTypeId"
                left outer join event_sessions es on es.id = ett."sessionId"
                inner join events e on e.id = er."eventId"
                where et.id = :eventTicketId
                """;
        String sqlCount = """
                select count(et.id)
                from event_tickets et
                inner join event_registrations er on er."id" = et."registrationId"
                inner join event_orders eo on eo."id" = er."orderId"
                inner join event_order_items eoi on eoi."orderId" = eo.id
                inner join event_ticket_types ett on ett.id = et."ticketTypeId"
                  --  inner join ticket_types tt on tt.id = et."ticketTypeId"
                left outer join event_sessions es on es.id = ett."sessionId"
                inner join events e on e.id = er."eventId"
                where et.id = :eventTicketId
                """;
        DataSourceAdapter ds = dsManager.get(meta.datasourceKey());
        Map<String, Object> params = Map.of("eventTicketId", eventTicketId);
        return ds.findAllBySql(sql,
                sqlCount,
                params,
                Map.of("eventTicketId", "et.id"),
                null,
                1,
                1,
                (rs, rowNum) -> EventTicketDetailResponse.builder()
                        .ticketId(rs.getObject("ticketId", UUID.class))
                        .ticketTypeId(rs.getObject("ticketTypeId", UUID.class))
                        .ticketNumber(rs.getString("ticketNumber"))
                        .status(rs.getString("status"))
                        .orderId(rs.getObject("orderId", UUID.class))
                        .orderNumber(rs.getString("orderNumber"))
                        .orderStatus(rs.getString("orderStatus"))
                        .ticketTypeName(rs.getString("ticketTypeName"))
                        .price(rs.getBigDecimal("price"))
                        .sessionTitle(rs.getString("sessionTitle"))
                        .sessionDescription(rs.getString("description"))
                        .sessionStartAt(toInstant(rs.getTimestamp("startAt")))
                        .sessionEndAt(toInstant(rs.getTimestamp("endAt")))
                        .eventTitle(rs.getString("eventTitle"))
                        .eventDescription(rs.getString("eventDescription"))
                        .eventSummary(rs.getString("eventSummary"))
                        .eventStartAt(toInstant(rs.getTimestamp("eventStartAt")))
                        .eventEndAt(toInstant(rs.getTimestamp("eventEndAt")))
                        .attendeeEmail(rs.getString("attendeeEmail"))
                        .attendeeFirstName(rs.getString("attendeeFirstName"))
                        .attendeeLastName(rs.getString("attendeeLastName"))
                        .build()
        );
    }
}
