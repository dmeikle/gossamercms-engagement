package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventOrderDto;
import com.gossamercms.events.models.EventOrder;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventOrdersDbService extends BaseDbService<EventOrder, EventOrderDto> {

    public EventOrdersDbService(DataSourceManager ds) {
        super(EventOrder.class, EventOrderDto.class, ds);
    }

    private NamedParameterJdbcTemplate jdbc() {
        return dsManager.get(meta.datasourceKey()).getNamedParameterJdbcTemplate();
    }

    public EventOrderDto order(UUID id, boolean lock) {
        var rows = jdbc().queryForList("SELECT * FROM event_orders WHERE \"id\"=:id AND \"deletedAt\" IS NULL"
                + (lock ? " FOR UPDATE" : ""), Map.of("id", id));
        if (rows.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        return EventDbRows.mapRowToDto(rows.getFirst(), EventOrderDto.class);
    }

    public EventOrderDto publicOrder(String hash) {
        var rows = jdbc().queryForList("""
                SELECT o.* FROM event_orders o JOIN event_order_access a ON a."orderId"=o."id"
                WHERE a."tokenHash"=:hash AND a."expiresAt">CURRENT_TIMESTAMP AND o."deletedAt" IS NULL
                """, Map.of("hash", hash));
        if (rows.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        return EventDbRows.mapRowToDto(rows.getFirst(), EventOrderDto.class);
    }

    public void createCheckout(EventOrderDto order) {
        jdbc().update("""
                INSERT INTO event_orders ("id","eventId","orderNumber","purchaserUserId","purchaserName",
                  "purchaserEmail","currency","subtotal","discountTotal","taxTotal","serviceFee","total","status","expiresAt")
                VALUES (:id,:event,:number,:user,:name,:email,:currency,:total,0,0,0,:total,'checkout_creating',:expires)
                """, Map.of("id", order.getId(), "event", order.getEventId(), "number", order.getOrderNumber(),
                "user", order.getPurchaserUserId(), "name", order.getPurchaserName(), "email", order.getPurchaserEmail(),
                "currency", order.getCurrency(), "total", order.getTotal(),
                "expires", Timestamp.from(order.getExpiresAt())));
    }

    public void createPublicAccess(UUID orderId, String hash, Instant expiresAt) {
        jdbc().update("""
                INSERT INTO event_order_access ("orderId","tokenHash","expiresAt") VALUES (:id,:hash,:expires)
                """, Map.of("id", orderId, "hash", hash, "expires", Timestamp.from(expiresAt)));
    }

    public void markCheckoutUnknown(UUID id) {
        jdbc().update("""
                UPDATE event_orders SET "status"='checkout_unknown',"updatedAt"=CURRENT_TIMESTAMP
                WHERE "id"=:id AND "status"='checkout_creating'
                """, Map.of("id", id));
    }

    public void claimCancellation(UUID id, String reason) {
        jdbc().update("""
                UPDATE event_orders SET "status"='cancelling',"cancelReason"=:reason,
                  "updatedAt"=CURRENT_TIMESTAMP WHERE "id"=:id
                """, Map.of("id", id, "reason", reason));
    }

    public void completeCancellation(UUID id) {
        jdbc().update("""
                UPDATE event_orders SET "status"='cancelled',"cancelledAt"=CURRENT_TIMESTAMP,
                  "updatedAt"=CURRENT_TIMESTAMP WHERE "id"=:id
                """, Map.of("id", id));
    }

    public void setStatus(UUID id, String status) {
        jdbc().update("UPDATE event_orders SET \"status\"=:status,\"updatedAt\"=CURRENT_TIMESTAMP WHERE \"id\"=:id",
                Map.of("id", id, "status", status));
    }

    @Override
    protected EventOrder mapToEntity(EventOrderDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventOrderDto mapToDto(EventOrder entity) {
        return entity.toDto();
    }

    @Override
    protected EventOrderDto removeExcludedFields(EventOrderDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
