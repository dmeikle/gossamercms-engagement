package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventOrdersDbService;
import com.gossamercms.events.dtos.EventOrderDto;
import com.gossamercms.events.models.EventOrder;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;
import com.gossamercms.events.services.EventOrderService;
import com.gossamercms.events.dtos.requests.*;
import com.gossamercms.events.dtos.responses.*;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import java.util.UUID;

@ModuleHandler
public class EventOrdersHandler extends BaseHandler<EventOrder, EventOrderDto> {

    private final EventOrderService orders;

    public EventOrdersHandler(EventOrdersDbService db, EventOrderService orders) {
        super(db);
        this.orders = orders;
    }

    public EventOrderDto authorizedOrder(CurrentJwtUser user, UUID id) {
        return orders.authorizedOrder(user, id);
    }

    public void requireManager(CurrentJwtUser user, UUID eventId) {
        orders.requireManager(user, eventId);
    }

    public EventOrderResponse response(EventOrderDto dto) {
        return orders.response(dto);
    }

    public PublicEventOrderResponse publicRead(String token) {
        return orders.publicRead(token);
    }

    public EventOrderCheckoutResponse checkout(CurrentJwtUser user, UUID eventId, EventOrderRequest request) {
        return orders.checkout(user, eventId, request);
    }

    public EventOrderResponse cancel(CurrentJwtUser user, UUID id, EventOrderCancelRequest request) {
        return orders.cancel(user, id, request);
    }

    public EventOrderRefundResponse refund(CurrentJwtUser user, UUID id, EventOrderRefundRequest request) {
        return orders.refund(user, id, request);
    }
}
