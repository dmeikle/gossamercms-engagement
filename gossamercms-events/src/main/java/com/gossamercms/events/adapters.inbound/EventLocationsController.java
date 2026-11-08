package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventLocationDto;
import com.gossamercms.events.dtos.requests.EventLocationRequest;
import com.gossamercms.events.dtos.responses.EventLocationResponse;
import com.gossamercms.events.handlers.EventLocationsHandler;
import com.gossamercms.events.models.EventLocation;
import com.gossamercms.mvc.controllers.TypedBaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/event-locations")
public class EventLocationsController extends TypedBaseController<EventLocation, EventLocationDto, EventLocationRequest, EventLocationResponse> {

    public EventLocationsController(EventLocationsHandler handler) {
        super(handler);
    }

    @Override
    protected EventLocationDto toDto(EventLocationRequest request) {
        return EventLocationDto.builder()
                .address1(request.getAddress1())
                .address2(request.getAddress2())
                .city(request.getCity())
                .stateProvince(request.getStateProvince())
                .postalCode(request.getPostalCode())
                .countryCode(request.getCountryCode())
                .eventId(request.getEventId())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .name(request.getName())
                .locationType(request.getLocationType())
                .onlineUrl(request.getOnlineUrl())
                .instructions(request.getInstructions())
                .build();

    }

    @Override
    protected EventLocationResponse toResponse(EventLocationDto dto) {
        return EventLocationResponse.builder()
                .id(dto.getId())
                .eventId(dto.getEventId())
                .name(dto.getName())
                .locationType(dto.getLocationType())
                .address1(dto.getAddress1())
                .address2(dto.getAddress2())
                .city(dto.getCity())
                .stateProvince(dto.getStateProvince())
                .postalCode(dto.getPostalCode())
                .countryCode(dto.getCountryCode())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .onlineUrl(dto.getOnlineUrl())
                .instructions(dto.getInstructions())
                .build();
    }

    @GetMapping
    public ApiResponse<ListResultset<EventLocationResponse>> listEventLocations(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam Map<String, Object> params
    ) {
        return ApiResponse.ok(_getAll(jwtUser, page, size, params));
    }

    @GetMapping("/{location}")
    public ApiResponse<EventLocationResponse> getEventLocation(
            @PathVariable("location") EventLocationDto existing
    ) {
        return super.getById(existing);
    }

}
