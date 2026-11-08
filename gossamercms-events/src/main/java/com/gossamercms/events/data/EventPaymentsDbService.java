package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventPaymentDto;
import com.gossamercms.events.models.EventPayment;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventPaymentsDbService extends BaseDbService<EventPayment, EventPaymentDto> {

    public EventPaymentsDbService(DataSourceManager ds) {
        super(EventPayment.class, EventPaymentDto.class, ds);
    }

    private NamedParameterJdbcTemplate jdbc() {
        return dsManager.get(meta.datasourceKey()).getNamedParameterJdbcTemplate();
    }

    public EventPayment payment(UUID orderId) {
        var rows = jdbc().queryForList("SELECT * FROM event_payments WHERE \"orderId\"=:id FOR UPDATE",
                Map.of("id", orderId));
        return rows.isEmpty() ? null : EventDbRows.mapRowToDto(rows.getFirst(), EventPaymentDto.class).toEntity();
    }

    public void createCheckout(UUID id, UUID orderId, String provider, BigDecimal amount, String currency) {
        jdbc().update("""
                INSERT INTO event_payments ("id","orderId","provider","amount","currency","status")
                VALUES (:id,:order,:provider,:amount,:currency,'checkout_creating')
                """, Map.of("id", id, "order", orderId, "provider", provider, "amount", amount, "currency", currency));
    }

    public void markCheckoutUnknown(UUID id) {
        jdbc().update("""
                UPDATE event_payments SET "status"='checkout_unknown',"updatedAt"=CURRENT_TIMESTAMP
                WHERE "id"=:id AND "status"='checkout_creating'
                """, Map.of("id", id));
    }

    public void completeCheckout(UUID id, String checkoutId) {
        jdbc().update("""
                UPDATE event_payments SET "providerCheckoutId"=:checkout,
                  "status"=CASE WHEN "status"='checkout_creating' THEN 'pending' ELSE "status" END,
                  "updatedAt"=CURRENT_TIMESTAMP WHERE "id"=:id
                """, Map.of("id", id, "checkout", checkoutId));
    }

    public void completeCancellation(UUID orderId) {
        jdbc().update("""
                UPDATE event_payments SET "status"='cancelled',"updatedAt"=CURRENT_TIMESTAMP WHERE "orderId"=:id
                """, Map.of("id", orderId));
    }

    public void setStatus(UUID id, String status) {
        jdbc().update("""
                UPDATE event_payments SET "status"=:status,"updatedAt"=CURRENT_TIMESTAMP WHERE "id"=:id
                """, Map.of("id", id, "status", status));
    }

    @Override
    protected EventPayment mapToEntity(EventPaymentDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventPaymentDto mapToDto(EventPayment entity) {
        return entity.toDto();
    }

    @Override
    protected EventPaymentDto removeExcludedFields(EventPaymentDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
