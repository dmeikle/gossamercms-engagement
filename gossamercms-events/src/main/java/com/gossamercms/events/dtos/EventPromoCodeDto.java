package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventPromoCode;
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
public class EventPromoCodeDto implements DtoWithId{
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
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public EventPromoCode toEntity() {
        return EventPromoCode.builder()
            .id(id)
            .eventId(eventId)
            .code(code)
            .discountType(discountType)
            .discountValue(discountValue)
            .currency(currency)
            .maximumRedemptions(maximumRedemptions)
            .redemptionCount(redemptionCount)
            .minimumOrderAmount(minimumOrderAmount)
            .validFrom(validFrom)
            .validUntil(validUntil)
            .isActive(isActive)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
}
