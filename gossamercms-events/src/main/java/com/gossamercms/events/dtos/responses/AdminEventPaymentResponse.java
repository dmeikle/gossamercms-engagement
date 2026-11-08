package com.gossamercms.events.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class AdminEventPaymentResponse {
    private UUID id;
    private UUID orderId;
    private String provider;
    private String providerPaymentId;
    private String providerCheckoutId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private Instant processedAt;
}
