package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventTicketTypeDto;
import com.gossamercms.events.dtos.responses.EventSessionOptionListResponse;
import com.gossamercms.events.dtos.responses.EventTicketTypeListResponse;
import com.gossamercms.events.dtos.responses.EventTicketTypeResponse;
import com.gossamercms.events.models.EventTicketType;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.*;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventTicketTypesDbService extends BaseDbService<EventTicketType, EventTicketTypeDto> {

    public EventTicketTypesDbService(DataSourceManager ds) {
        super(EventTicketType.class, EventTicketTypeDto.class, ds);
    }

    private NamedParameterJdbcTemplate jdbc() {
        return dsManager.get(meta.datasourceKey()).getNamedParameterJdbcTemplate();
    }

    public EventTicketType ticket(UUID id) {
        // Deleted types must remain lockable so existing reservations can be released.
        var rows = jdbc().queryForList("""
                SELECT * FROM event_ticket_types WHERE "id"=:id FOR UPDATE
                """, Map.of("id", id));
        if (rows.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ticket type");
        return EventDbRows.mapRowToDto(rows.getFirst(), EventTicketTypeDto.class).toEntity();
    }

    public void reserveInventory(UUID id, int quantity) {
        jdbc().update("""
                UPDATE event_ticket_types SET "quantityReserved"=COALESCE("quantityReserved",0)+:quantity,
                  "updatedAt"=CURRENT_TIMESTAMP WHERE "id"=:id
                """, Map.of("id", id, "quantity", quantity));
    }

    public void releaseInventory(UUID id, int quantity) {
        int changed = jdbc().update("""
                UPDATE event_ticket_types SET "quantityReserved"=COALESCE("quantityReserved",0)-:quantity,
                  "updatedAt"=CURRENT_TIMESTAMP
                WHERE "id"=:id AND COALESCE("quantityReserved",0)>=:quantity
                """, Map.of("id", id, "quantity", quantity));
        if (changed != 1) throw new ResponseStatusException(HttpStatus.CONFLICT, "Inventory needs reconciliation");
    }

    public void completeReservation(UUID id, int quantity) {
        int changed = jdbc().update("""
                UPDATE event_ticket_types
                SET "quantityReserved"=COALESCE("quantityReserved",0)-:quantity,
                    "quantitySold"=COALESCE("quantitySold",0)+:quantity,
                    "updatedAt"=CURRENT_TIMESTAMP
                WHERE "id"=:id AND COALESCE("quantityReserved",0)>=:quantity
                  AND COALESCE("quantitySold",0)<=2147483647-:quantity
                """, Map.of("id", id, "quantity", quantity));
        if (changed != 1) throw new ResponseStatusException(HttpStatus.CONFLICT, "Inventory needs reconciliation");
    }

    @Override
    protected EventTicketType mapToEntity(EventTicketTypeDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventTicketTypeDto mapToDto(EventTicketType entity) {
        return entity.toDto();
    }

    @Override
    protected EventTicketTypeDto removeExcludedFields(EventTicketTypeDto dto) {
        return dto;
    }



    public ListResultset<EventTicketTypeListResponse> listTicketTypesByEvent(QueryOptions options) {
        DataSourceAdapter ds = dsManager.get(meta.datasourceKey());
        String sql = """
                select ett.id, ett."eventId", "sessionId", es.title as "sessionTitle", name, ett.id as "ticketTypeId", price, currency, "quantityAvailable", "quantityReserved", "quantitySold",
                  "minimumPerOrder", "maximumPerOrder", "salesStartAt", "salesEndAt", ett."status", "isVisible"
                  from event_ticket_types ett
                   inner join event_sessions es on es."id" = ett."sessionId"
                /*WHERE_CLAUSE*/
                order by title
                """;

        String countSql = """
               select count(ett.id)
                  from event_ticket_types ett
                   inner join event_sessions es on es."id" = ett."sessionId"
                /*WHERE_CLAUSE*/
               """;

        Map<String, Object> params = Map.of("ett.eventId", options.filters().get("eventId"));

        return ds.findAllBySql(sql,
                countSql,
                params,
                Collections.emptyMap(),
                options.orderBy(),
                options.page(),
                options.size(),
                (rs, rownum) -> EventTicketTypeListResponse.builder()
                        .id(rs.getObject("id", UUID.class))
                        .currency(rs.getString("currency"))
                        .eventId(rs.getObject("eventId", UUID.class))
                        .sessionId(rs.getObject("sessionId", UUID.class))
                        .sessionTitle(rs.getString("sessionTitle"))
                        .ticketTypeId(rs.getObject("ticketTypeId", UUID.class))
                        .name(rs.getString("name"))
                        .price(rs.getObject("price", BigDecimal.class))
                        .quantityAvailable(rs.getObject("quantityAvailable", Integer.class))
                        .quantityReserved(rs.getObject("quantityReserved", Integer.class))
                        .quantitySold(rs.getObject("quantitySold", Integer.class))
                        .minimumPerOrder(rs.getObject("minimumPerOrder", Integer.class))
                        .maximumPerOrder(rs.getObject("maximumPerOrder", Integer.class))
                        .salesStartAt(toInstant(rs.getTimestamp("salesStartAt")))
                        .salesEndAt(toInstant(rs.getTimestamp("salesEndAt")))
                        .status(rs.getString("status"))
                        .isVisible(rs.getObject("isVisible", Boolean.class))
                        .build()
        );

    }
}
