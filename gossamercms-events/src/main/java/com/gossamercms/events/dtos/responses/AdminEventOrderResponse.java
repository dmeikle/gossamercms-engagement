package com.gossamercms.events.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class AdminEventOrderResponse {
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
}
