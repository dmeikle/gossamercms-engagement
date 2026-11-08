package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventRegistrationDto;
import com.gossamercms.events.dtos.responses.AdminEventRegistrationResponse;
import com.gossamercms.events.dtos.requests.AdminEventRegistrationRequest;
import com.gossamercms.events.handlers.EventRegistrationsHandler;
import com.gossamercms.events.models.EventRegistration;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/event-registrations")
public class AdminEventRegistrationsController extends BaseController<EventRegistration, EventRegistrationDto, AdminEventRegistrationRequest, AdminEventRegistrationResponse, AdminEventRegistrationResponse> {

    public AdminEventRegistrationsController(EventRegistrationsHandler handler) {
        super(handler, AdminEventRegistrationResponse.class);
    }

    @Override
    protected EventRegistrationDto toDto(AdminEventRegistrationRequest request) {
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
    protected AdminEventRegistrationResponse toResponse(EventRegistrationDto dto) {
        return AdminEventRegistrationResponse.builder()
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

}
