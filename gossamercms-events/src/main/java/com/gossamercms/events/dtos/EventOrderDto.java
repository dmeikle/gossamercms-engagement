package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventOrder;
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
public class EventOrderDto implements DtoWithId{
    private UUID id;
    private UUID eventId;
    private String orderNumber;
    private UUID purchaserUserContextId;
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

    public EventOrder toEntity() {
        return EventOrder.builder()
            .id(id)
            .eventId(eventId)
            .orderNumber(orderNumber)
            .purchaserUserContextId(purchaserUserContextId)
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
}
