package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.events.dtos.EventTicketTypeDto;
import com.gossamercms.events.dtos.responses.AdminEventTicketTypeResponse;
import com.gossamercms.events.dtos.requests.AdminEventTicketTypeRequest;
import com.gossamercms.events.dtos.responses.EventTicketTypeResponse;
import com.gossamercms.events.handlers.EventTicketTypesHandler;
import com.gossamercms.events.models.EventTicketType;
import com.gossamercms.mvc.controllers.BaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.mvc.http.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/admin/event-ticket-types")
public class AdminEventTicketTypesController extends BaseController<EventTicketType, EventTicketTypeDto, AdminEventTicketTypeRequest, AdminEventTicketTypeResponse, AdminEventTicketTypeResponse> {

    public AdminEventTicketTypesController(EventTicketTypesHandler handler) {
        super(handler, AdminEventTicketTypeResponse.class);
    }

    @Override
    protected EventTicketTypeDto toDto(AdminEventTicketTypeRequest request) {
        return EventTicketTypeDto.builder()
                .eventId(request.getEventId())
                .sessionId(request.getSessionId())
                .name(request.getName())
                .description(request.getDescription())
                .ticketTypeId(request.getTicketTypeId())
                .price(request.getPrice())
                .currency(request.getCurrency())
                .quantityAvailable(request.getQuantityAvailable())
                .quantityReserved(request.getQuantityReserved())
                .quantitySold(request.getQuantitySold())
                .minimumPerOrder(request.getMinimumPerOrder())
                .maximumPerOrder(request.getMaximumPerOrder())
                .salesStartAt(request.getSalesStartAt())
                .salesEndAt(request.getSalesEndAt())
                .status(request.getStatus())
                .sortOrder(request.getSortOrder())
                .isVisible(request.getIsVisible())
                .build();
    }

    @Override
    protected AdminEventTicketTypeResponse toResponse(EventTicketTypeDto dto) {
        return AdminEventTicketTypeResponse.builder()
                .id(dto.getId())
                .eventId(dto.getEventId())
                .sessionId(dto.getSessionId())
                .name(dto.getName())
                .description(dto.getDescription())
                .ticketTypeId(dto.getTicketTypeId())
                .price(dto.getPrice())
                .currency(dto.getCurrency())
                .quantityAvailable(dto.getQuantityAvailable())
                .quantityReserved(dto.getQuantityReserved())
                .quantitySold(dto.getQuantitySold())
                .minimumPerOrder(dto.getMinimumPerOrder())
                .maximumPerOrder(dto.getMaximumPerOrder())
                .salesStartAt(dto.getSalesStartAt())
                .salesEndAt(dto.getSalesEndAt())
                .status(dto.getStatus())
                .sortOrder(dto.getSortOrder())
                .isVisible(dto.getIsVisible())
                .build();
    }

    @GetMapping("{event}/list-types")
    public ApiResponse<ListResultset<EventTicketTypeResponse>> listTypes(@PathVariable(name = "event") EventDto event) {
        ListResultset<EventTicketTypeResponse> types = this.handler.getAll(QueryOptions.builder().filters(Map.of("eventId", event.getId())).build()
        , EventTicketTypeResponse.class);
        return ApiResponse.ok(types);
    }
}
