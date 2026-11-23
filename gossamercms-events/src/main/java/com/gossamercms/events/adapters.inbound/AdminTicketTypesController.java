package com.gossamercms.events.adapters.inbound;

import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.events.dtos.responses.TicketTypeResponse;
import com.gossamercms.events.handlers.TicketTypesHandler;
import com.gossamercms.mvc.controllers.BaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.mvc.http.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gossamercms.events.models.TicketType;
import com.gossamercms.events.dtos.TicketTypeDto;
import com.gossamercms.events.dtos.requests.TicketTypeRequest;

import javax.management.Query;
import java.util.Map;


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

    @GetMapping("/event/{event}")
    public ApiResponse<ListResultset<TicketTypeResponse>> listTicketTypesForEvent(
            @PathVariable("event") EventDto event) {

        return ApiResponse.ok(handler.getAll(QueryOptions.builder()
                .filters(Map.of("eventId", event.getId()))
                .build(), TicketTypeResponse.class));
    }
}
