package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventRefund;
import com.gossamercms.mvc.data.DtoWithId;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRefundDto implements DtoWithId{
    private UUID id;
    private UUID paymentId;
    private String providerRefundId;
    private BigDecimal amount;
    private String currency;
    private String reason;
    private String status;
    private Instant refundedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public EventRefund toEntity() {
        return EventRefund.builder()
            .id(id)
            .paymentId(paymentId)
            .providerRefundId(providerRefundId)
            .amount(amount)
            .currency(currency)
            .reason(reason)
            .status(status)
            .refundedAt(refundedAt)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
    }
}
