package com.gossamercms.events.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class AdminEventRefundResponse {
    private UUID id;
    private UUID paymentId;
    private String providerRefundId;
    private BigDecimal amount;
    private String currency;
    private String reason;
    private String status;
    private Instant refundedAt;
}
