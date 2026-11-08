package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventLocationDto;
import com.gossamercms.events.dtos.requests.EventLocationRequest;
import com.gossamercms.events.dtos.responses.EventLocationResponse;
import com.gossamercms.events.handlers.EventLocationsHandler;
import com.gossamercms.events.models.EventLocation;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.controllers.BaseController;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/event-locations")
public class AdminEventLocationsController extends BaseController<EventLocation, EventLocationDto, EventLocationRequest, EventLocationResponse, EventLocationResponse> {

    public AdminEventLocationsController(EventLocationsHandler handler) {
        super(handler, EventLocationResponse.class);
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

    @Override
    @GetMapping("/{location}")
    public ApiResponse<EventLocationResponse> getById(
            @PathVariable("location") EventLocationDto existing
    ) {
        return ApiResponse.ok(toResponse(existing));
    }


    @Override
    @DeleteMapping("/{location}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void deleteById(
            @CurrentUser CurrentJwtUser jwtUser,
            @PathVariable("location") EventLocationDto existing
    ) {
        super.deleteById(jwtUser, existing);
    }
}
