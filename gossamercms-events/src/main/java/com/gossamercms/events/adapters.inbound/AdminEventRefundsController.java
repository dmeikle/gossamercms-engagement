package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventRefundDto;
import com.gossamercms.events.dtos.responses.AdminEventRefundResponse;
import com.gossamercms.events.dtos.requests.AdminEventRefundRequest;
import com.gossamercms.events.handlers.EventRefundsHandler;
import com.gossamercms.events.models.EventRefund;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-refunds")
public class AdminEventRefundsController extends BaseController<EventRefund, EventRefundDto, AdminEventRefundRequest, AdminEventRefundResponse, AdminEventRefundResponse> {

    public AdminEventRefundsController(EventRefundsHandler handler) {
        super(handler, AdminEventRefundResponse.class);
    }

    @Override
    protected EventRefundDto toDto(AdminEventRefundRequest request) {
        return EventRefundDto.builder()
                .paymentId(request.getPaymentId())
                .providerRefundId(request.getProviderRefundId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .reason(request.getReason())
                .status(request.getStatus())
                .refundedAt(request.getRefundedAt())
                .build();
    }

    @Override
    protected AdminEventRefundResponse toResponse(EventRefundDto dto) {
        return AdminEventRefundResponse.builder()
                .id(dto.getId())
                .paymentId(dto.getPaymentId())
                .providerRefundId(dto.getProviderRefundId())
                .amount(dto.getAmount())
                .currency(dto.getCurrency())
                .reason(dto.getReason())
                .status(dto.getStatus())
                .refundedAt(dto.getRefundedAt())
                .build();
    }

}
