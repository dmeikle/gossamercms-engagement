package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventOrganizerDto;
import com.gossamercms.events.dtos.EventOrganizerMemberDto;
import com.gossamercms.events.dtos.requests.EventOrganizerMemberRequest;
import com.gossamercms.events.dtos.requests.EventOrganizerRequest;
import com.gossamercms.events.dtos.responses.EventListDto;
import com.gossamercms.events.dtos.responses.EventOrganizerResponse;
import com.gossamercms.events.handlers.EventOrganizerMembersHandler;
import com.gossamercms.events.handlers.EventOrganizersHandler;
import com.gossamercms.events.handlers.EventsHandler;
import com.gossamercms.events.models.EventOrganizer;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.controllers.TypedBaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/event-organizers")
public class EventOrganizersController extends TypedBaseController<
        EventOrganizer, EventOrganizerDto, EventOrganizerRequest, EventOrganizerResponse> {

    private final EventOrganizerMembersHandler eventOrganizerMembersHandler;
    private final EventsHandler eventsHandler;

    public EventOrganizersController(EventOrganizersHandler handler,
                                     EventOrganizerMembersHandler eventOrganizerMembersHandler,
                                     EventsHandler eventsHandler) {
        super(handler);
        this.eventOrganizerMembersHandler = eventOrganizerMembersHandler;
        this.eventsHandler = eventsHandler;
    }

    @Override
    protected EventOrganizerDto toDto(EventOrganizerRequest request) {
        return EventOrganizerDto.builder()
                .organizationId(request.getOrganizationId())
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .email(request.getEmail())
                .phone(request.getPhone())
                .website(request.getWebsite())
                .status("active")
                .build();
    }

    @Override
    protected EventOrganizerResponse toResponse(EventOrganizerDto dto) {
        return EventOrganizerResponse.builder()
                .id(dto.getId())
                .organizationId(dto.getOrganizationId())
                .name(dto.getName())
                .slug(dto.getSlug())
                .description(dto.getDescription())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .website(dto.getWebsite())
                .logoUrl(dto.getLogoUrl())
                .status(dto.getStatus())
                .build();
    }

    @GetMapping
    public ApiResponse<ListResultset<EventOrganizerResponse>> getAll(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam Map<String, Object> params) {
        return ApiResponse.ok(_getAll(jwtUser, page, size, params));
    }

    @GetMapping("/{organizer}")
    public ApiResponse<EventOrganizerResponse> getById(@PathVariable("organizer") EventOrganizerDto existing) {
        return ApiResponse.ok(toResponse(existing));
    }

    @PostMapping
    public ApiResponse<EventOrganizerResponse> create(
            @CurrentUser CurrentJwtUser jwtUser,
            @RequestBody EventOrganizerRequest request) {
        return ApiResponse.ok(_create(jwtUser, request));
    }

    @PutMapping("/{organizer}")
    public ApiResponse<EventOrganizerResponse> updateById(
            @CurrentUser CurrentJwtUser jwtUser,
            @PathVariable("organizer") EventOrganizerDto existing,
            @RequestBody EventOrganizerRequest request) {
        // Merge supplied fields so omitted and internal fields (status, Stripe, audit) are preserved.


        return ApiResponse.ok(toResponse(
                handler.updateById(
                        jwtUser.getUserId(),
                        existing.getId(),
                        EventOrganizerDto.builder()
                                .id(existing.getId())
                                .organizationId(request.getOrganizationId() != null ? request.getOrganizationId() : existing.getOrganizationId())
                                .name(request.getName() != null ? request.getName() : existing.getName())
                                .slug(request.getSlug() != null ? request.getSlug() : existing.getSlug())
                                .description(request.getDescription() != null ? request.getDescription() : existing.getDescription())
                                .email(request.getEmail() != null ? request.getEmail() : existing.getEmail())
                                .phone(request.getPhone() != null ? request.getPhone() : existing.getPhone())
                                .website(request.getWebsite() != null ? request.getWebsite() : existing.getWebsite())
                                .logoUrl(existing.getLogoUrl()) // Preserve logo URL
                                .status(existing.getStatus()) // Preserve status
                                .stripeAccountId(existing.getStripeAccountId())
                                .stripeChargesEnabled(existing.getStripeChargesEnabled())
                                .stripePayoutsEnabled(existing.getStripePayoutsEnabled())
                                .stripeDetailsSubmitted(existing.getStripeDetailsSubmitted())
                                .build()
                )));
    }

    /**********************************
     *  Organizer Methods
     **********************************/

    @GetMapping("/{organizer}/events")
    public ApiResponse<ListResultset<EventListDto>> getEvents(
            @PathVariable("organizer") EventOrganizerDto existing,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {
        return ApiResponse.ok(eventsHandler.getAll(QueryOptions.builder()
                .page(page)
                .size(size)
                .filters(Map.of("organizerId", existing.getId()))
                .build(), EventListDto.class));
    }

    @GetMapping("/{organizer}/members")
    public ApiResponse<ListResultset<EventOrganizerMemberDto>> getMembers(
            @PathVariable("organizer") EventOrganizerDto existing,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {
        return ApiResponse.ok(eventOrganizerMembersHandler.getAll(
                QueryOptions.builder()
                .page(page)
                .size(size)
                .filters(Map.of("organizerId", existing.getId()))
                .build(), EventOrganizerMemberDto.class));
    }

    @PostMapping("/{organizer}/members")
    public ApiResponse<EventOrganizerMemberDto> inviteMember(
            @CurrentUser CurrentJwtUser jwtUser,
            @PathVariable("organizer") EventOrganizerDto existing,
            @RequestBody EventOrganizerMemberRequest request) {

        return ApiResponse.ok(
                eventOrganizerMembersHandler.create(
                        jwtUser.getUserId(),
                        EventOrganizerMemberDto.builder()
                                .userId(request.userId())
                                .role(request.role())
                                .organizerId(existing.getId())
                                .status("invited")
                                .invitedAt(Instant.now())
                            .build()));
    }

    @DeleteMapping("/{organizer}/members/{userId}")
    public void removeMember(
            @CurrentUser CurrentJwtUser jwtUser,
            @PathVariable("organizer") EventOrganizerDto existing,
            @PathVariable("userId") UUID userId) {
        eventOrganizerMembersHandler.delete(jwtUser.getUserId(), Map.of("organizerId", existing.getId(), "userId", userId));
    }


}
