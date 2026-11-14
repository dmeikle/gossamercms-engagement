package com.gossamercms.events.adapters.inbound;

import com.gossamercms.events.dtos.EventTicketTypeDto;
import com.gossamercms.events.dtos.requests.EventTicketTypeRequest;
import com.gossamercms.events.dtos.responses.EventTicketTypeResponse;
import com.gossamercms.events.handlers.EventTicketTypesHandler;
import com.gossamercms.events.models.EventTicketType;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.controllers.TypedBaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/event-ticket-types")
public class EventTicketTypesController extends TypedBaseController<
        EventTicketType, EventTicketTypeDto, EventTicketTypeRequest, EventTicketTypeResponse, EventTicketTypeResponse> {

    public EventTicketTypesController(EventTicketTypesHandler handler) {
        super(handler, EventTicketTypeResponse.class);
    }

    @Override
    protected EventTicketTypeDto toDto(EventTicketTypeRequest request) {
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
    protected EventTicketTypeResponse toResponse(EventTicketTypeDto dto) {
        return EventTicketTypeResponse.builder()
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


    @GetMapping
    public ApiResponse<ListResultset<EventTicketTypeResponse>> getAll(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam Map<String, Object> params) {
        return ApiResponse.ok(_getAll(jwtUser, page, size, params));
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<EventTicketTypeResponse> getById(@PathVariable("id") EventTicketTypeDto existing) {
        return super.getById(existing);
    }
}
