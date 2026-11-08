package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventOrderDto;
import com.gossamercms.events.dtos.responses.AdminEventOrderResponse;
import com.gossamercms.events.dtos.requests.AdminEventOrderRequest;
import com.gossamercms.events.handlers.EventOrdersHandler;
import com.gossamercms.events.models.EventOrder;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-orders")
public class AdminEventOrdersController extends BaseController<EventOrder, EventOrderDto, AdminEventOrderRequest, AdminEventOrderResponse, AdminEventOrderResponse> {

    public AdminEventOrdersController(EventOrdersHandler handler) {
        super(handler, AdminEventOrderResponse.class);
    }

    @Override
    protected EventOrderDto toDto(AdminEventOrderRequest request) {
        return EventOrderDto.builder()
                .eventId(request.getEventId())
                .orderNumber(request.getOrderNumber())
                .purchaserUserId(request.getPurchaserUserId())
                .purchaserName(request.getPurchaserName())
                .purchaserEmail(request.getPurchaserEmail())
                .currency(request.getCurrency())
                .subtotal(request.getSubtotal())
                .discountTotal(request.getDiscountTotal())
                .taxTotal(request.getTaxTotal())
                .serviceFee(request.getServiceFee())
                .total(request.getTotal())
                .status(request.getStatus())
                .expiresAt(request.getExpiresAt())
                .paidAt(request.getPaidAt())
                .cancelledAt(request.getCancelledAt())
                .build();
    }

    @Override
    protected AdminEventOrderResponse toResponse(EventOrderDto dto) {
        return AdminEventOrderResponse.builder()
                .id(dto.getId())
                .eventId(dto.getEventId())
                .orderNumber(dto.getOrderNumber())
                .purchaserUserId(dto.getPurchaserUserId())
                .purchaserName(dto.getPurchaserName())
                .purchaserEmail(dto.getPurchaserEmail())
                .currency(dto.getCurrency())
                .subtotal(dto.getSubtotal())
                .discountTotal(dto.getDiscountTotal())
                .taxTotal(dto.getTaxTotal())
                .serviceFee(dto.getServiceFee())
                .total(dto.getTotal())
                .status(dto.getStatus())
                .expiresAt(dto.getExpiresAt())
                .paidAt(dto.getPaidAt())
                .cancelledAt(dto.getCancelledAt())
                .build();
    }

}
