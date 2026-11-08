package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventPayment;
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
public class EventPaymentDto implements DtoWithId{
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

    public EventPayment toEntity() {
        return EventPayment.builder()
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
}
