package com.gossamercms.events.adapters.inbound;

import com.gossamercms.events.dtos.responses.TicketTypeResponse;
import com.gossamercms.events.handlers.TicketTypesHandler;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gossamercms.events.models.TicketType;
import com.gossamercms.events.dtos.TicketTypeDto;
import com.gossamercms.events.dtos.requests.TicketTypeRequest;

@RestController
@RequestMapping("/admin/ticket-types")
public class AdminTicketTypesController extends BaseController<
        TicketType,
        TicketTypeDto,
        TicketTypeRequest,
        TicketTypeResponse,
        TicketTypeResponse> {

    public AdminTicketTypesController(TicketTypesHandler handler) {
        super(handler, TicketTypeResponse.class);
    }

    @Override
    protected TicketTypeDto toDto(TicketTypeRequest request) {
        return  TicketTypeDto.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    @Override
    protected TicketTypeResponse toResponse(TicketTypeDto dto) {
        return TicketTypeResponse.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }
}
