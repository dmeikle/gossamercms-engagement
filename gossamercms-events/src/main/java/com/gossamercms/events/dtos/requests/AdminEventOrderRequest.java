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
public class AdminEventOrderRequest {
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
}
