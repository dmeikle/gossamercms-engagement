package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventTicketTypeDto;
import com.gossamercms.events.dtos.responses.AdminEventTicketTypeResponse;
import com.gossamercms.events.dtos.requests.AdminEventTicketTypeRequest;
import com.gossamercms.events.handlers.EventTicketTypesHandler;
import com.gossamercms.events.models.EventTicketType;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
                .ticketType(request.getTicketType())
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
                .ticketType(dto.getTicketType())
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

}
