package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventPaymentDto;
import com.gossamercms.events.dtos.requests.EventPaymentRequest;
import com.gossamercms.events.dtos.responses.EventPaymentResponse;
import com.gossamercms.events.handlers.EventPaymentsHandler;
import com.gossamercms.events.models.EventPayment;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.controllers.TypedBaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/event-payments")
public class EventPaymentsController extends TypedBaseController<
        EventPayment, EventPaymentDto, EventPaymentRequest, EventPaymentResponse> {

    public EventPaymentsController(EventPaymentsHandler handler) {
        super(handler);
    }

    @Override
    protected EventPaymentDto toDto(EventPaymentRequest request) {
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
    protected EventPaymentResponse toResponse(EventPaymentDto dto) {
        return EventPaymentResponse.builder()
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

    @GetMapping
    public ApiResponse<ListResultset<EventPaymentResponse>> getAll(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam Map<String, Object> params) {
        return ApiResponse.ok(_getAll(jwtUser, page, size, params));
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<EventPaymentResponse> getById(@PathVariable("id") EventPaymentDto existing) {
        return super.getById(existing);
    }
}
