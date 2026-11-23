package com.gossamercms.events.services;

import com.gossamercms.events.data.EventLocationsDbService;
import com.gossamercms.events.data.EventSessionsDbService;
import com.gossamercms.events.data.EventTicketTypesDbService;
import com.gossamercms.events.data.EventsDbService;
import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.events.dtos.responses.EventDetailWithSessionsResponse;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventDetailsWithSessionsTest {
    @Test
    void returnsQueriedEventDetailsWithSessions() {
        var sessions = mock(EventSessionsDbService.class);
        var locations = mock(EventLocationsDbService.class);
        var ticketTypes = mock(EventTicketTypesDbService.class);
        var service = new EventsService(mock(EventsDbService.class), locations, ticketTypes, sessions);
        UUID eventId = UUID.randomUUID();
        var event = EventDto.builder().id(eventId).title("Event").slug("event").build();
        var ticket = new EventDetailWithSessionsResponse.EventTicketTypeDto(
                UUID.randomUUID(), "General", "active", "CAD", 10, 0, null, 0);
        var details = EventDetailWithSessionsResponse.builder().id(eventId).title("Event")
                .eventTicketTypes(List.of(ticket)).eventSessions(List.of()).build();
        when(sessions.getFullDetails(event)).thenReturn(details);

        var result = service.getDetailsWithSessions(event);

        assertEquals(eventId, result.getId());
        assertEquals("Event", result.getTitle());
        assertSame(details, result);
        assertEquals(List.of(ticket), result.getEventTicketTypes());
        assertTrue(result.getEventSessions().isEmpty());
        verifyNoInteractions(ticketTypes, locations);
    }
}
