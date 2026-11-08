package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventTicketDto;
import com.gossamercms.events.dtos.responses.AdminEventTicketResponse;
import com.gossamercms.events.dtos.requests.AdminEventTicketRequest;
import com.gossamercms.events.handlers.EventTicketsHandler;
import com.gossamercms.events.models.EventTicket;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-tickets")
public class AdminEventTicketsController extends BaseController<EventTicket, EventTicketDto, AdminEventTicketRequest, AdminEventTicketResponse, AdminEventTicketResponse> {

    public AdminEventTicketsController(EventTicketsHandler handler) {
        super(handler, AdminEventTicketResponse.class);
    }

    @Override
    protected EventTicketDto toDto(AdminEventTicketRequest request) {
        return EventTicketDto.builder()
                .registrationId(request.getRegistrationId())
                .ticketTypeId(request.getTicketTypeId())
                .ticketNumber(request.getTicketNumber())
                .qrTokenHash(request.getQrTokenHash())
                .status(request.getStatus())
                .issuedAt(request.getIssuedAt())
                .transferredAt(request.getTransferredAt())
                .cancelledAt(request.getCancelledAt())
                .build();
    }

    @Override
    protected AdminEventTicketResponse toResponse(EventTicketDto dto) {
        return AdminEventTicketResponse.builder()
                .id(dto.getId())
                .registrationId(dto.getRegistrationId())
                .ticketTypeId(dto.getTicketTypeId())
                .ticketNumber(dto.getTicketNumber())
                .qrTokenHash(dto.getQrTokenHash())
                .status(dto.getStatus())
                .issuedAt(dto.getIssuedAt())
                .transferredAt(dto.getTransferredAt())
                .cancelledAt(dto.getCancelledAt())
                .build();
    }

}
