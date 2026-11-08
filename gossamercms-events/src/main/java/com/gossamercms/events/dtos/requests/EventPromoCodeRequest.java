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
public class EventPromoCodeRequest {
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
