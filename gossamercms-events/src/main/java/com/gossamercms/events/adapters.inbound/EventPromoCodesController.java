package com.gossamercms.events.adapters.inbound;

import com.gossamercms.events.dtos.EventPromoCodeDto;
import com.gossamercms.events.dtos.requests.EventPromoCodeRequest;
import com.gossamercms.events.dtos.responses.EventPromoCodeResponse;
import com.gossamercms.events.handlers.EventPromoCodesHandler;
import com.gossamercms.events.models.EventPromoCode;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.controllers.TypedBaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/event-promo-codes")
public class EventPromoCodesController extends TypedBaseController<
        EventPromoCode, EventPromoCodeDto, EventPromoCodeRequest, EventPromoCodeResponse> {

    public EventPromoCodesController(EventPromoCodesHandler handler) {
        super(handler);
    }

    @Override
    protected EventPromoCodeDto toDto(EventPromoCodeRequest request) {
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
    protected EventPromoCodeResponse toResponse(EventPromoCodeDto dto) {
        return EventPromoCodeResponse.builder()
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


    @GetMapping
    public ApiResponse<ListResultset<EventPromoCodeResponse>> getAll(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam Map<String, Object> params) {
        return ApiResponse.ok(_getAll(jwtUser, page, size, params));
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<EventPromoCodeResponse> getById(@PathVariable("id") EventPromoCodeDto existing) {
        return super.getById(existing);
    }
}
