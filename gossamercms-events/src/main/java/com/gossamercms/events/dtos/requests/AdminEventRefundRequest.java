package com.gossamercms.events.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEventRefundRequest {
    private UUID paymentId;
    private String providerRefundId;
    private BigDecimal amount;
    private String currency;
    private String reason;
    private String status;
    private Instant refundedAt;
}
