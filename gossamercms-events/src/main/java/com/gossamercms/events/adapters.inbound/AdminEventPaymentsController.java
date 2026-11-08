package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventPaymentDto;
import com.gossamercms.events.dtos.responses.AdminEventPaymentResponse;
import com.gossamercms.events.dtos.requests.AdminEventPaymentRequest;
import com.gossamercms.events.handlers.EventPaymentsHandler;
import com.gossamercms.events.models.EventPayment;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-payments")
public class AdminEventPaymentsController extends BaseController<EventPayment, EventPaymentDto, AdminEventPaymentRequest, AdminEventPaymentResponse, AdminEventPaymentResponse> {

    public AdminEventPaymentsController(EventPaymentsHandler handler) {
        super(handler, AdminEventPaymentResponse.class);
    }

    @Override
    protected EventPaymentDto toDto(AdminEventPaymentRequest request) {
        return EventPaymentDto.builder()
                .orderId(request.getOrderId())
                .provider(request.getProvider())
                .providerPaymentId(request.getProviderPaymentId())
                .providerCheckoutId(request.getProviderCheckoutId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status(request.getStatus())
                .processedAt(request.getProcessedAt())
                .build();
    }

    @Override
    protected AdminEventPaymentResponse toResponse(EventPaymentDto dto) {
        return AdminEventPaymentResponse.builder()
                .id(dto.getId())
                .orderId(dto.getOrderId())
                .provider(dto.getProvider())
                .providerPaymentId(dto.getProviderPaymentId())
                .providerCheckoutId(dto.getProviderCheckoutId())
                .amount(dto.getAmount())
                .currency(dto.getCurrency())
                .status(dto.getStatus())
                .processedAt(dto.getProcessedAt())
                .build();
    }

}
