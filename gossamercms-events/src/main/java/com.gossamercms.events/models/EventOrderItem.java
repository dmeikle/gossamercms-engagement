package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventOrderItemDto;
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
public class EventOrderItem implements BaseModel{
    private UUID id;
    private UUID orderId;
    private UUID ticketTypeId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private Instant createdAt;

    public EventOrderItemDto toDto() {
        return EventOrderItemDto.builder()
            .id(id)
            .orderId(orderId)
            .ticketTypeId(ticketTypeId)
            .quantity(quantity)
            .unitPrice(unitPrice)
            .lineTotal(lineTotal)
            .createdAt(createdAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("event_order_items")
    .column("id", UUID.class)
    .column("orderId", UUID.class)
    .column("ticketTypeId", UUID.class)
    .column("quantity", Integer.class)
    .column("unitPrice", BigDecimal.class)
    .column("lineTotal", BigDecimal.class)
    .column("createdAt", Instant.class)
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
