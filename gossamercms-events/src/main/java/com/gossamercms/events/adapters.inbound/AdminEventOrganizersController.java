package com.gossamercms.events.adapters.inbound;


import com.gossamercms.events.dtos.EventOrganizerDto;
import com.gossamercms.events.dtos.responses.AdminEventOrganizerResponse;
import com.gossamercms.events.dtos.requests.AdminEventOrganizerRequest;
import com.gossamercms.events.dtos.EventOrganizerMemberDto;
import com.gossamercms.events.dtos.requests.EventOrganizerMemberRequest;
import com.gossamercms.events.dtos.responses.EventListDto;
import com.gossamercms.events.handlers.EventOrganizerMembersHandler;
import com.gossamercms.events.handlers.EventOrganizersHandler;
import com.gossamercms.events.handlers.EventsHandler;
import com.gossamercms.events.models.EventOrganizer;
import com.gossamercms.mvc.annotations.CurrentUser;
import com.gossamercms.mvc.controllers.BaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/admin/event-organizers")
public class AdminEventOrganizersController extends BaseController<EventOrganizer, EventOrganizerDto, AdminEventOrganizerRequest, AdminEventOrganizerResponse, AdminEventOrganizerResponse> {

    private final EventOrganizerMembersHandler eventOrganizerMembersHandler;
    private final EventsHandler eventsHandler;

    public AdminEventOrganizersController(EventOrganizersHandler handler,
                                     EventOrganizerMembersHandler eventOrganizerMembersHandler,
                                     EventsHandler eventsHandler) {
        super(handler, AdminEventOrganizerResponse.class);
        this.eventOrganizerMembersHandler = eventOrganizerMembersHandler;
        this.eventsHandler = eventsHandler;
    }

    @Override
    protected EventOrganizerDto toDto(AdminEventOrganizerRequest request) {
        return EventOrganizerDto.builder()
                .organizationId(request.getOrganizationId())
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .email(request.getEmail())
                .phone(request.getPhone())
                .website(request.getWebsite())
                .logoUrl(request.getLogoUrl())
                .status(request.getStatus())
                .stripeAccountId(request.getStripeAccountId())
                .stripeChargesEnabled(request.getStripeChargesEnabled())
                .stripePayoutsEnabled(request.getStripePayoutsEnabled())
                .stripeDetailsSubmitted(request.getStripeDetailsSubmitted())
                .build();
    }

    @Override
    protected AdminEventOrganizerResponse toResponse(EventOrganizerDto dto) {
        return AdminEventOrganizerResponse.builder()
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
                .stripeAccountId(dto.getStripeAccountId())
                .stripeChargesEnabled(dto.getStripeChargesEnabled())
                .stripePayoutsEnabled(dto.getStripePayoutsEnabled())
                .stripeDetailsSubmitted(dto.getStripeDetailsSubmitted())
                .build();
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
