package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventOrderDto;
import com.gossamercms.events.handlers.EventOrdersHandler;
import com.gossamercms.events.models.EventOrder;
import com.gossamercms.events.dtos.requests.*;
import com.gossamercms.events.dtos.responses.*;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.controllers.TypedBaseController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
public class EventOrdersController extends TypedBaseController<
        EventOrder, EventOrderDto, EventOrderRequest, EventOrderResponse> {

    private final EventOrdersHandler orders;

    public EventOrdersController(EventOrdersHandler handler) {
        super(handler);
        this.orders = handler;
    }

    @Override
    protected EventOrderDto toDto(EventOrderRequest request) {
        return EventOrderDto.builder().purchaserName(request.name()).purchaserEmail(request.email())
                .currency(request.currency()).build();
    }

    @Override
    protected EventOrderResponse toResponse(EventOrderDto dto) {
        return orders.response(dto);
    }

    @PostMapping("/events/{eventId}/orders")
    public ApiResponse<EventOrderCheckoutResponse> checkout(
            @CurrentUser CurrentJwtUser user, @PathVariable("eventId") UUID eventId, @RequestBody EventOrderRequest request) {
        return ApiResponse.ok(orders.checkout(user, eventId, request));
    }

    @GetMapping("/events/{eventId}/orders")
    public ApiResponse<ListResultset<EventOrderResponse>> list(
            @CurrentUser CurrentJwtUser user, @PathVariable("eventId") UUID eventId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {
        orders.requireManager(user, eventId);
        if (page < 1 || size < 1 || size > 100 || (long) (page - 1) * size > Integer.MAX_VALUE)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid pagination");
        // Caller-controlled filters must never override the authorized event scope.
        return ApiResponse.ok(_getAll(user, page, size, new HashMap<>(Map.of("eventId", eventId))));
    }

    @GetMapping("/event-orders/{orderId}")
    public ApiResponse<EventOrderResponse> read(@CurrentUser CurrentJwtUser user, @PathVariable("orderId") UUID orderId) {
        return super.getById(orders.authorizedOrder(user, orderId));
    }

    @GetMapping("/event-orders/public/{accessToken}")
    public ApiResponse<PublicEventOrderResponse> publicRead(@PathVariable("accessToken") String accessToken) {
        return ApiResponse.ok(orders.publicRead(accessToken));
    }

    @PostMapping("/event-orders/{orderId}/cancel")
    public ApiResponse<EventOrderResponse> cancel(
            @CurrentUser CurrentJwtUser user, @PathVariable("orderId") UUID orderId, @RequestBody EventOrderCancelRequest request) {
        return ApiResponse.ok(orders.cancel(user, orderId, request));
    }

    @PostMapping("/event-orders/{orderId}/refund")
    public ApiResponse<EventOrderRefundResponse> refund(
            @CurrentUser CurrentJwtUser user, @PathVariable("orderId") UUID orderId, @RequestBody EventOrderRefundRequest request) {
        return ApiResponse.ok(orders.refund(user, orderId, request));
    }
}
