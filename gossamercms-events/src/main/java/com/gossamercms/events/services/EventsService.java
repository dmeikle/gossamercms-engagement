package com.gossamercms.events.services;

import com.gossamercms.events.data.EventLocationsDbService;
import com.gossamercms.events.data.EventTicketTypesDbService;
import com.gossamercms.events.data.EventTicketsDbService;
import com.gossamercms.events.data.EventsDbService;
import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.events.dtos.EventLocationDto;
import com.gossamercms.events.dtos.EventTicketTypeDto;
import com.gossamercms.events.dtos.requests.EventRequest;
import com.gossamercms.events.dtos.responses.*;
import com.gossamercms.mvc.annotations.ModuleService;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@ModuleService
@RequiredArgsConstructor
public class EventsService {
    private final EventsDbService eventsDbService;
    private final EventLocationsDbService eventLocationsDbService;
    private final EventTicketTypesDbService eventTicketTypesDbService;
    private final EventTicketsDbService eventTicketsDbService;

    public AdminEventResponse create(CurrentJwtUser jwtUser, EventRequest request) {
        EventDto event = eventsDbService.create(jwtUser.getUserContextId(), EventDto.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .startAt(request.getStartAt())
            .endAt(request.getEndAt())
            .registrationStartAt(request.getRegistrationStartAt())
            .registrationEndAt(request.getRegistrationEndAt())
            .isFeatured(request.getIsFeatured())
            .isPublished(request.getIsPublished())
            .createdBy(jwtUser.getUserId())
            .bannerImageUrl(request.getBannerImageUrl())
            .eventType(request.getEventType())
            .slug(request.getSlug())
            .status(request.getStatus())
            .summary(request.getSummary())
            .thumbnailImageUrl(request.getThumbnailImageUrl())
            .timezone(request.getTimezone())
            .visibility(request.getVisibility())
            .timezone(request.getTimezone())
            .build());

        EventLocationDto location = eventLocationsDbService.create(event.getId(), EventLocationDto.builder()
                .address1(request.getLocation().getAddress1())
                .address2(request.getLocation().getAddress2())
                .city(request.getLocation().getCity())
                .stateProvince(request.getLocation().getStateProvince())
                .countryCode(request.getLocation().getCountryCode())
                .postalCode(request.getLocation().getPostalCode())
                .latitude(request.getLocation().getLatitude())
                .longitude(request.getLocation().getLongitude())
                .eventId(event.getId())
                .build());

        return AdminEventResponse.builder()
                .title(event.getTitle())
                .visibility(event.getVisibility())
                .isPublished(event.getIsPublished())
                .bannerImageUrl(event.getBannerImageUrl())
                .description(event.getDescription())
                .endAt(event.getEndAt())
                .eventType(event.getEventType())
                .id(event.getId())
                .isFeatured(event.getIsFeatured())
                .registrationStartAt(event.getRegistrationStartAt())
                .registrationEndAt(event.getRegistrationEndAt())
                .slug(event.getSlug())
                .startAt(event.getStartAt())
                .status(event.getStatus())
                .summary(event.getSummary())
                .thumbnailImageUrl(event.getThumbnailImageUrl())
                .timezone(event.getTimezone())
                .location(EventLocationResponse.builder()
                        .address1(location.getAddress1())
                        .address2(location.getAddress2())
                        .city(location.getCity())
                        .stateProvince(location.getStateProvince())
                        .countryCode(location.getCountryCode())
                        .postalCode(location.getPostalCode())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .eventId(location.getEventId())
                        .build())
                .build();
    }

    public AdminEventResponse update(CurrentJwtUser jwtUser, EventDto existing, EventRequest request) {
        EventDto updatedEvent = eventsDbService.update(jwtUser.getUserContextId(), EventDto.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .registrationStartAt(request.getRegistrationStartAt())
                .registrationEndAt(request.getRegistrationEndAt())
                .isFeatured(request.getIsFeatured())
                .isPublished(request.getIsPublished())
                .bannerImageUrl(request.getBannerImageUrl())
                .eventType(request.getEventType())
                .slug(request.getSlug())
                .status(request.getStatus())
                .summary(request.getSummary())
                .thumbnailImageUrl(request.getThumbnailImageUrl())
                .timezone(request.getTimezone())
                .visibility(request.getVisibility())
                .build(),
                Map.of("id", existing.getId()));
        EventLocationDto existingLocation = eventLocationsDbService.getOrNull(Map.of("eventId", existing.getId()));
        if(existingLocation == null) {
            EventLocationDto newLocation = eventLocationsDbService.create(updatedEvent.getId(), EventLocationDto.builder()
                    .address1(request.getLocation().getAddress1())
                    .address2(request.getLocation().getAddress2())
                    .city(request.getLocation().getCity())
                    .stateProvince(request.getLocation().getStateProvince())
                    .countryCode(request.getLocation().getCountryCode())
                    .postalCode(request.getLocation().getPostalCode())
                    .latitude(request.getLocation().getLatitude())
                    .longitude(request.getLocation().getLongitude())
                    .eventId(updatedEvent.getId())
                    .build());
            return AdminEventResponse.builder()
                    .title(updatedEvent.getTitle())
                    .visibility(updatedEvent.getVisibility())
                    .isPublished(updatedEvent.getIsPublished())
                    .bannerImageUrl(updatedEvent.getBannerImageUrl())
                    .description(updatedEvent.getDescription())
                    .endAt(updatedEvent.getEndAt())
                    .eventType(updatedEvent.getEventType())
                    .id(updatedEvent.getId())
                    .isFeatured(updatedEvent.getIsFeatured())
                    .registrationStartAt(updatedEvent.getRegistrationStartAt())
                    .registrationEndAt(updatedEvent.getRegistrationEndAt())
                    .slug(updatedEvent.getSlug())
                    .startAt(updatedEvent.getStartAt())
                    .status(updatedEvent.getStatus())
                    .summary(updatedEvent.getSummary())
                    .thumbnailImageUrl(updatedEvent.getThumbnailImageUrl())
                    .timezone(updatedEvent.getTimezone())
                    .location(EventLocationResponse.builder()
                            .address1(newLocation.getAddress1())
                            .address2(newLocation.getAddress2())
                            .city(newLocation.getCity())
                            .stateProvince(newLocation.getStateProvince())
                            .countryCode(newLocation.getCountryCode())
                            .postalCode(newLocation.getPostalCode())
                            .latitude(newLocation.getLatitude())
                            .longitude(newLocation.getLongitude())
                            .eventId(newLocation.getEventId())
                            .build())
                    .build();
        }
        EventLocationDto updatedLocation = eventLocationsDbService.update(jwtUser.getUserContextId(), EventLocationDto.builder()
                .address1(request.getLocation().getAddress1())
                .address2(request.getLocation().getAddress2())
                .city(request.getLocation().getCity())
                .stateProvince(request.getLocation().getStateProvince())
                .countryCode(request.getLocation().getCountryCode())
                .postalCode(request.getLocation().getPostalCode())
                .latitude(request.getLocation().getLatitude())
                .longitude(request.getLocation().getLongitude())
                .eventId(updatedEvent.getId())
                .build(),
                Map.of("eventId", existing.getId()));

        return AdminEventResponse.builder()
                .title(updatedEvent.getTitle())
                .visibility(updatedEvent.getVisibility())
                .isPublished(updatedEvent.getIsPublished())
                .bannerImageUrl(updatedEvent.getBannerImageUrl())
                .description(updatedEvent.getDescription())
                .endAt(updatedEvent.getEndAt())
                .eventType(updatedEvent.getEventType())
                .id(updatedEvent.getId())
                .isFeatured(updatedEvent.getIsFeatured())
                .registrationStartAt(updatedEvent.getRegistrationStartAt())
                .registrationEndAt(updatedEvent.getRegistrationEndAt())
                .slug(updatedEvent.getSlug())
                .startAt(updatedEvent.getStartAt())
                .status(updatedEvent.getStatus())
                .summary(updatedEvent.getSummary())
                .thumbnailImageUrl(updatedEvent.getThumbnailImageUrl())
                .timezone(updatedEvent.getTimezone())
                .location(EventLocationResponse.builder()
                        .address1(updatedLocation.getAddress1())
                        .address2(updatedLocation.getAddress2())
                        .city(updatedLocation.getCity())
                        .stateProvince(updatedLocation.getStateProvince())
                        .countryCode(updatedLocation.getCountryCode())
                        .postalCode(updatedLocation.getPostalCode())
                        .latitude(updatedLocation.getLatitude())
                        .longitude(updatedLocation.getLongitude())
                        .eventId(updatedLocation.getEventId())
                        .build())
                .build();
    }

    public AdminEventResponse getDetails(EventDto existing) {
        EventLocationDto existingLocation = eventLocationsDbService.getOrNull(Map.of("eventId", existing.getId()));

        return AdminEventResponse.builder()
                .title(existing.getTitle())
                .visibility(existing.getVisibility())
                .isPublished(existing.getIsPublished())
                .bannerImageUrl(existing.getBannerImageUrl())
                .description(existing.getDescription())
                .endAt(existing.getEndAt())
                .eventType(existing.getEventType())
                .id(existing.getId())
                .isFeatured(existing.getIsFeatured())
                .registrationStartAt(existing.getRegistrationStartAt())
                .registrationEndAt(existing.getRegistrationEndAt())
                .slug(existing.getSlug())
                .startAt(existing.getStartAt())
                .status(existing.getStatus())
                .summary(existing.getSummary())
                .thumbnailImageUrl(existing.getThumbnailImageUrl())
                .timezone(existing.getTimezone())
                .location(getLocationResponse(existing))
                .eventTicketTypes(getEventTicketTypes(existing))
                .build();
    }



    private List<EventTicketTypeResponse> getEventTicketTypes(EventDto event) {
        ListResultset<EventTicketTypeDto> eventTicketTypes = eventTicketTypesDbService.getAll(
                QueryOptions.builder()
                        .filters(Map.of("eventId", event.getId()))
                        .build()
        );

        return eventTicketTypes.list().stream().map(tt -> EventTicketTypeResponse.builder()
                .id(tt.getId())
                .name(tt.getName())
                .description(tt.getDescription())
                .price(tt.getPrice())
                .quantityAvailable(tt.getQuantityAvailable())
                .currency(tt.getCurrency())
                .eventId(tt.getEventId())
                .isVisible(tt.getIsVisible())
                .maximumPerOrder(tt.getMaximumPerOrder())
                .minimumPerOrder(tt.getMinimumPerOrder())
                .quantityReserved(tt.getQuantityReserved())
                .quantitySold(tt.getQuantitySold())
                .salesEndAt(tt.getSalesEndAt())
                .salesStartAt(tt.getSalesStartAt())
                .sessionId(tt.getSessionId() != null ? tt.getSessionId() : null)
                .status(tt.getStatus() != null ? tt.getStatus() : null)
                .ticketTypeId(tt.getTicketTypeId() != null ? tt.getTicketTypeId() : null)
                .build()).toList();
    }

    private EventLocationResponse getLocationResponse(EventDto event) {
        EventLocationDto location = eventLocationsDbService.getOrNull(Map.of("eventId", event.getId()));
        if(location == null) {
            return null;
        }
        return EventLocationResponse.builder()
                .address1(location.getAddress1())
                .address2(location.getAddress2())
                .city(location.getCity())
                .stateProvince(location.getStateProvince())
                .countryCode(location.getCountryCode())
                .postalCode(location.getPostalCode())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .eventId(location.getEventId())
                .build();
    }
}