package com.gossamercms.events.adapters.inbound;

import com.gossamercms.events.dtos.EventRegistrationDto;
import com.gossamercms.events.dtos.requests.EventRegistrationRequest;
import com.gossamercms.events.dtos.responses.EventRegistrationResponse;
import com.gossamercms.events.handlers.EventRegistrationsHandler;
import com.gossamercms.events.models.EventRegistration;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.controllers.TypedBaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/event-registrations")
public class EventRegistrationsController extends TypedBaseController<
        EventRegistration, EventRegistrationDto, EventRegistrationRequest, EventRegistrationResponse> {

    public EventRegistrationsController(EventRegistrationsHandler handler) {
        super(handler);
    }

    @Override
    protected EventRegistrationDto toDto(EventRegistrationRequest request) {
        return EventRegistrationDto.builder()
                .eventId(request.getEventId())
                .orderId(request.getOrderId())
                .orderItemId(request.getOrderItemId())
                .userId(request.getUserId())
                .attendeeFirstName(request.getAttendeeFirstName())
                .attendeeLastName(request.getAttendeeLastName())
                .attendeeEmail(request.getAttendeeEmail())
                .status(request.getStatus())
                .registeredAt(request.getRegisteredAt())
                .cancelledAt(request.getCancelledAt())
                .build();
    }

    @Override
    protected EventRegistrationResponse toResponse(EventRegistrationDto dto) {
        return EventRegistrationResponse.builder()
                .id(dto.getId())
                .eventId(dto.getEventId())
                .orderId(dto.getOrderId())
                .orderItemId(dto.getOrderItemId())
                .userId(dto.getUserId())
                .attendeeFirstName(dto.getAttendeeFirstName())
                .attendeeLastName(dto.getAttendeeLastName())
                .attendeeEmail(dto.getAttendeeEmail())
                .status(dto.getStatus())
                .registeredAt(dto.getRegisteredAt())
                .cancelledAt(dto.getCancelledAt())
                .build();
    }


    @GetMapping
    public ApiResponse<ListResultset<EventRegistrationResponse>> getAll(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam Map<String, Object> params) {
        return ApiResponse.ok(_getAll(jwtUser, page, size, params));
    }

    @Override
    @GetMapping("/{id}")
    public ApiResponse<EventRegistrationResponse> getById(@PathVariable("id") EventRegistrationDto existing) {
        return super.getById(existing);
    }
}
