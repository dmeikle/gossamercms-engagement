package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventPaymentDto;
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
public class EventPayment implements BaseModel{
    private UUID id;
    private UUID orderId;
    private String provider;
    private String providerPaymentId;
    private String providerCheckoutId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private Instant processedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public EventPaymentDto toDto() {
        return EventPaymentDto.builder()
            .id(id)
            .orderId(orderId)
            .provider(provider)
            .providerPaymentId(providerPaymentId)
            .providerCheckoutId(providerCheckoutId)
            .amount(amount)
            .currency(currency)
            .status(status)
            .processedAt(processedAt)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("event_payments")
    .column("id", UUID.class)
    .column("orderId", UUID.class)
    .column("provider", String.class)
    .column("providerPaymentId", String.class, 255)
    .column("providerCheckoutId", String.class, 255)
    .column("amount", BigDecimal.class)
    .column("currency", String.class, 3)
    .column("status", String.class)
    .column("processedAt", Instant.class)
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
