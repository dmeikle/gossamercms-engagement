package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventPromoCodeDto;
import com.gossamercms.events.dtos.responses.AdminEventPromoCodeResponse;
import com.gossamercms.events.dtos.requests.AdminEventPromoCodeRequest;
import com.gossamercms.events.handlers.EventPromoCodesHandler;
import com.gossamercms.events.models.EventPromoCode;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-promo-codes")
public class AdminEventPromoCodesController extends BaseController<EventPromoCode, EventPromoCodeDto, AdminEventPromoCodeRequest, AdminEventPromoCodeResponse, AdminEventPromoCodeResponse> {

    public AdminEventPromoCodesController(EventPromoCodesHandler handler) {
        super(handler, AdminEventPromoCodeResponse.class);
    }

    @Override
    protected EventPromoCodeDto toDto(AdminEventPromoCodeRequest request) {
        return EventPromoCodeDto.builder()
                .eventId(request.getEventId())
                .code(request.getCode())
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .currency(request.getCurrency())
                .maximumRedemptions(request.getMaximumRedemptions())
                .redemptionCount(request.getRedemptionCount())
                .minimumOrderAmount(request.getMinimumOrderAmount())
                .validFrom(request.getValidFrom())
                .validUntil(request.getValidUntil())
                .isActive(request.getIsActive())
                .build();
    }

    @Override
    protected AdminEventPromoCodeResponse toResponse(EventPromoCodeDto dto) {
        return AdminEventPromoCodeResponse.builder()
                .id(dto.getId())
                .eventId(dto.getEventId())
                .code(dto.getCode())
                .discountType(dto.getDiscountType())
                .discountValue(dto.getDiscountValue())
                .currency(dto.getCurrency())
                .maximumRedemptions(dto.getMaximumRedemptions())
                .redemptionCount(dto.getRedemptionCount())
                .minimumOrderAmount(dto.getMinimumOrderAmount())
                .validFrom(dto.getValidFrom())
                .validUntil(dto.getValidUntil())
                .isActive(dto.getIsActive())
                .build();
    }

}
