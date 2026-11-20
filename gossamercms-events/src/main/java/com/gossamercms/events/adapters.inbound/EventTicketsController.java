package com.gossamercms.events.adapters.inbound;

import com.gossamercms.events.dtos.EventTicketDto;
import com.gossamercms.events.dtos.requests.EventTicketRequest;
import com.gossamercms.events.dtos.responses.EventTicketListResponse;
import com.gossamercms.events.dtos.responses.EventTicketResponse;
import com.gossamercms.events.handlers.EventTicketsHandler;
import com.gossamercms.events.models.EventTicket;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.controllers.TypedBaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/event-tickets")
public class EventTicketsController extends TypedBaseController<
        EventTicket, EventTicketDto, EventTicketRequest, EventTicketResponse, EventTicketListResponse> {

    public EventTicketsController(EventTicketsHandler handler) {
        super(handler, EventTicketListResponse.class);
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
    public ApiResponse<ListResultset<EventTicketListResponse>> getAll(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam Map<String, Object> params) {

        if (jwtUser == null || jwtUser.getUserContextId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "A user context is required");
        }
        params.remove("page");
        params.remove("size");
        params.put("userContextId", jwtUser.getUserContextId());

        return ApiResponse.ok(((EventTicketsHandler) handler)
                .listByUserContext(
                        QueryOptions.builder()
                                .page(page)
                                .size(size)
                                .filters(params)
                                .build()));
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<EventTicketResponse> getById(@PathVariable("id") EventTicketDto existing) {
        return super.getById(existing);
    }
}
