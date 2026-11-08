package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventRefundDto;
import com.gossamercms.events.models.EventRefund;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventRefundsDbService extends BaseDbService<EventRefund, EventRefundDto> {

    public EventRefundsDbService(DataSourceManager ds) {
        super(EventRefund.class, EventRefundDto.class, ds);
    }

    private NamedParameterJdbcTemplate jdbc() {
        return dsManager.get(meta.datasourceKey()).getNamedParameterJdbcTemplate();
    }

    public EventRefund inflightRefund(UUID paymentId) {
        var rows = jdbc().queryForList("""
                SELECT * FROM event_refunds WHERE "paymentId"=:id AND "status"='processing' FOR UPDATE
                """, Map.of("id", paymentId));
        return rows.isEmpty() ? null : EventDbRows.mapRowToDto(rows.getFirst(), EventRefundDto.class).toEntity();
    }

    public BigDecimal refunded(UUID paymentId) {
        return jdbc().queryForObject("""
                SELECT COALESCE(SUM("amount"),0) FROM event_refunds
                WHERE "paymentId"=:id AND "status" IN ('refunded','succeeded','processing')
                """, Map.of("id", paymentId), BigDecimal.class);
    }

    public boolean refundHistoryNeedsReconciliation(UUID paymentId) {
        return Boolean.TRUE.equals(jdbc().queryForObject("""
                SELECT EXISTS (SELECT 1 FROM event_refunds WHERE "paymentId"=:id AND (
                  "status" IS NULL OR "status" NOT IN ('refunded','succeeded','processing','failed','cancelled')
                  OR ("status" IN ('refunded','succeeded','processing') AND ("amount" IS NULL OR "amount"<=0))
                ))
                """, Map.of("id", paymentId), Boolean.class));
    }

    public void createProcessing(EventRefund refund) {
        jdbc().update("""
                INSERT INTO event_refunds ("id","paymentId","amount","currency","reason","status")
                VALUES (:id,:payment,:amount,:currency,:reason,'processing')
                """, Map.of("id", refund.getId(), "payment", refund.getPaymentId(), "amount", refund.getAmount(),
                "currency", refund.getCurrency(), "reason", refund.getReason()));
    }

    public void completeRefund(UUID id, String providerRefundId) {
        jdbc().update("""
                UPDATE event_refunds SET "providerRefundId"=:provider,"status"='refunded',
                  "refundedAt"=CURRENT_TIMESTAMP,"updatedAt"=CURRENT_TIMESTAMP
                WHERE "id"=:id AND "status"='processing'
                """, Map.of("id", id, "provider", providerRefundId));
    }

    @Override
    protected EventRefund mapToEntity(EventRefundDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventRefundDto mapToDto(EventRefund entity) {
        return entity.toDto();
    }

    @Override
    protected EventRefundDto removeExcludedFields(EventRefundDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
