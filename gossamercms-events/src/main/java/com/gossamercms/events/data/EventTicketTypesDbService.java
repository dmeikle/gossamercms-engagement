package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventTicketTypeDto;
import com.gossamercms.events.models.EventTicketType;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

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

    @Override
    protected void throw404(String id) {

    }

}
