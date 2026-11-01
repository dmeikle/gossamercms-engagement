package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventRefundDto;
import com.gossamercms.mvc.models.BaseModel;
import com.gossamercms.mvc.models.ModelMeta;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRefund implements BaseModel{
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

    public EventRefundDto toDto() {
        return EventRefundDto.builder()
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


public static final ModelMeta META = ModelMeta.builderWithId("event_refunds")
    .column("id", UUID.class)
    .column("paymentId", UUID.class)
    .column("providerRefundId", String.class, 255)
    .column("amount", BigDecimal.class)
    .column("currency", String.class, 3)
    .column("reason", String.class, 500)
    .column("status", String.class)
    .column("refundedAt", Instant.class)
    .column("createdAt", Instant.class)
    .column("updatedAt", Instant.class)
    .defaultSort("id desc")
    .build();



    @Override
    public ModelMeta meta() {
        return META;
    }

    @Override
    public ModelMeta metaOf() {
        return BaseModel.super.metaOf();
    }

}
