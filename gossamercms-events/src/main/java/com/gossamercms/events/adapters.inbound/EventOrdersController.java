package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventDto;
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
@RequestMapping("/event-orders")
public class EventOrdersController extends TypedBaseController<
        EventOrder, EventOrderDto, EventOrderRequest, EventOrderResponse, EventOrderResponse> {

    private final EventOrdersHandler orders;

    public EventOrdersController(EventOrdersHandler handler) {
        super(handler, EventOrderResponse.class);
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

    @PostMapping("/{event}/order")
    public ApiResponse<EventOrderCheckoutResponse> checkout(
            @CurrentUser CurrentJwtUser user, @PathVariable("event") EventDto event, @RequestBody EventOrderRequest request) {
        return ApiResponse.ok(orders.checkout(user, event.getId(), request));
    }

    @GetMapping("/{event}/orders")
    public ApiResponse<ListResultset<EventOrderResponse>> list(
            @CurrentUser CurrentJwtUser user, @PathVariable("event") EventDto event,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {
        orders.requireManager(user, event.getId());
        if (page < 1 || size < 1 || size > 100 || (long) (page - 1) * size > Integer.MAX_VALUE)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid pagination");
        // Caller-controlled filters must never override the authorized event scope.
        return ApiResponse.ok(_getAll(user, page, size, new HashMap<>(Map.of("eventId", event.getId()))));
    }

    @GetMapping("/{order}")
    public ApiResponse<EventOrderResponse> read(@CurrentUser CurrentJwtUser user, @PathVariable("order") EventOrderDto order) {
        return super.getById(orders.authorizedOrder(user, order.getId()));
    }

    @GetMapping("/public/{accessToken}")
    public ApiResponse<PublicEventOrderResponse> publicRead(@PathVariable("accessToken") String accessToken) {
        return ApiResponse.ok(orders.publicRead(accessToken));
    }

    @PostMapping("/{order}/cancel")
    public ApiResponse<EventOrderResponse> cancel(
            @CurrentUser CurrentJwtUser user, @PathVariable("order") EventOrderDto order, @RequestBody EventOrderCancelRequest request) {
        return ApiResponse.ok(orders.cancel(user, order.getId(), request));
    }

    @PostMapping("/{order}/refund")
    public ApiResponse<EventOrderRefundResponse> refund(
            @CurrentUser CurrentJwtUser user, @PathVariable("order") EventOrderDto order, @RequestBody EventOrderRefundRequest request) {
        return ApiResponse.ok(orders.refund(user, order.getId(), request));
    }
}
