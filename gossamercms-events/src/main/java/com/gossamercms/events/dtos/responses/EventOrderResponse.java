package com.gossamercms.events.dtos.responses;

import com.gossamercms.events.dtos.EventOrderDto;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record EventOrderResponse(UUID id, UUID eventId, String orderNumber, String name, String email,
                                 String currency, BigDecimal subtotal, BigDecimal total, String status,
                                 Instant expiresAt, Instant paidAt, Instant cancelledAt, List<Item> items) {
    public static EventOrderResponse from(EventOrderDto dto, List<Item> items) {
        return new EventOrderResponse(dto.getId(), dto.getEventId(), dto.getOrderNumber(),
                dto.getPurchaserName(), dto.getPurchaserEmail(), dto.getCurrency(), dto.getSubtotal(),
                dto.getTotal(), dto.getStatus(), dto.getExpiresAt(), dto.getPaidAt(), dto.getCancelledAt(), items);
    }

    public record Item(UUID id, UUID ticketTypeId, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {}
}
