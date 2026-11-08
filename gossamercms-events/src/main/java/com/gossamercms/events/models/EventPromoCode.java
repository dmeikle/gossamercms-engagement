package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventPromoCodeDto;
import com.gossamercms.mvc.models.BaseModel;
import com.gossamercms.mvc.models.ModelMeta;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventPromoCode implements BaseModel{
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

    public EventPromoCodeDto toDto() {
        return EventPromoCodeDto.builder()
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


public static final ModelMeta META = ModelMeta.builderWithId("event_promo_codes")
    .column("id", UUID.class)
    .column("eventId", UUID.class)
    .column("code", String.class, 50)
    .column("discountType", String.class)
    .column("discountValue", BigDecimal.class)
    .column("currency", String.class, 3)
    .column("maximumRedemptions", Integer.class)
    .column("redemptionCount", Integer.class)
    .column("minimumOrderAmount", BigDecimal.class)
    .column("validFrom", Instant.class)
    .column("validUntil", Instant.class)
    .column("isActive", Boolean.class)
    .column("createdAt", Instant.class)
    .column("updatedAt", Instant.class)
    .column("deletedAt", Instant.class)
    .defaultSort("id desc")
    .build();



    @Override
    public ModelMeta meta() {
        return META;
    }

    @Override
    public ModelMeta metaOf() {
        return BaseModel.super.metaOf();
    }

}
