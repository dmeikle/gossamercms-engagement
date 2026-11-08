package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventOrderDto;
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
public class EventOrder implements BaseModel{
    private UUID id;
    private UUID eventId;
    private String orderNumber;
    private UUID purchaserUserId;
    private String purchaserName;
    private String purchaserEmail;
    private String currency;
    private BigDecimal subtotal;
    private BigDecimal discountTotal;
    private BigDecimal taxTotal;
    private BigDecimal serviceFee;
    private BigDecimal total;
    private String status;
    private Instant expiresAt;
    private Instant paidAt;
    private Instant cancelledAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public EventOrderDto toDto() {
        return EventOrderDto.builder()
            .id(id)
            .eventId(eventId)
            .orderNumber(orderNumber)
            .purchaserUserId(purchaserUserId)
            .purchaserName(purchaserName)
            .purchaserEmail(purchaserEmail)
            .currency(currency)
            .subtotal(subtotal)
            .discountTotal(discountTotal)
            .taxTotal(taxTotal)
            .serviceFee(serviceFee)
            .total(total)
            .status(status)
            .expiresAt(expiresAt)
            .paidAt(paidAt)
            .cancelledAt(cancelledAt)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("event_orders")
    .column("id", UUID.class)
    .column("eventId", UUID.class)
    .column("orderNumber", String.class, 50)
    .column("purchaserUserId", UUID.class)
    .column("purchaserName", String.class, 255)
    .column("purchaserEmail", String.class, 255)
    .column("currency", String.class, 3)
    .column("subtotal", BigDecimal.class)
    .column("discountTotal", BigDecimal.class)
    .column("taxTotal", BigDecimal.class)
    .column("serviceFee", BigDecimal.class)
    .column("total", BigDecimal.class)
    .column("status", String.class)
    .column("expiresAt", Instant.class)
    .column("paidAt", Instant.class)
    .column("cancelledAt", Instant.class)
    .column("createdAt", Instant.class)
    .column("updatedAt", Instant.class)
    .column("deletedAt", Instant.class)
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
