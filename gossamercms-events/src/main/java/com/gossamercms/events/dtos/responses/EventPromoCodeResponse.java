package com.gossamercms.events.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EventPromoCodeResponse {
    private UUID id;
    private UUID eventId;
    private String code;
    private String discountType;
    private BigDecimal discountValue;
    private String currency;
    private Integer maximumRedemptions;
    private Integer redemptionCount;
    private BigDecimal minimumOrderAmount;
    private Instant validFrom;
    private Instant validUntil;
    private Boolean isActive;
}
