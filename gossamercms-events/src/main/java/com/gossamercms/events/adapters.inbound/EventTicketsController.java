package com.gossamercms.events.adapters.inbound;

import com.gossamercms.events.dtos.EventTicketDto;
import com.gossamercms.events.dtos.requests.EventTicketRequest;
import com.gossamercms.events.dtos.responses.EventTicketResponse;
import com.gossamercms.events.handlers.EventTicketsHandler;
import com.gossamercms.events.models.EventTicket;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.controllers.TypedBaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/event-tickets")
public class EventTicketsController extends TypedBaseController<
        EventTicket, EventTicketDto, EventTicketRequest, EventTicketResponse> {

    public EventTicketsController(EventTicketsHandler handler) {
        super(handler);
    }

    @Override
    protected EventTicketDto toDto(EventTicketRequest request) {
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
    protected EventTicketResponse toResponse(EventTicketDto dto) {
        return EventTicketResponse.builder()
                .id(dto.getId())
                .registrationId(dto.getRegistrationId())
                .ticketTypeId(dto.getTicketTypeId())
                .ticketNumber(dto.getTicketNumber())
                .status(dto.getStatus())
                .issuedAt(dto.getIssuedAt())
                .transferredAt(dto.getTransferredAt())
                .cancelledAt(dto.getCancelledAt())
                .build();
    }


    @GetMapping
    public ApiResponse<ListResultset<EventTicketResponse>> getAll(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam Map<String, Object> params) {
        return ApiResponse.ok(_getAll(jwtUser, page, size, params));
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<EventTicketResponse> getById(@PathVariable("id") EventTicketDto existing) {
        return super.getById(existing);
    }
}
