package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventOrderItem;
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
public class EventOrderItemDto implements DtoWithId{
    private UUID id;
    private UUID orderId;
    private UUID ticketTypeId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private Instant createdAt;

    public EventOrderItem toEntity() {
        return EventOrderItem.builder()
            .id(id)
            .orderId(orderId)
            .ticketTypeId(ticketTypeId)
            .quantity(quantity)
            .unitPrice(unitPrice)
            .lineTotal(lineTotal)
            .createdAt(createdAt)
            .build();
    }
}
