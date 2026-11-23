package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.events.dtos.responses.EventDetailWithSessionsResponse;
import com.gossamercms.mvc.data.DataSourceAdapter;
import com.gossamercms.mvc.data.DataSourceManager;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EventSessionsDbServiceTest {
    @Test
    void mapsTicketTypesSessionsAndLocationsFromJson() throws Exception {
        UUID eventId = UUID.randomUUID();
        UUID ticketId = UUID.randomUUID();
        UUID sessionTicketId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        UUID locationId = UUID.randomUUID();
        String ticketJson = """
                [{"id":"%s","name":"General","status":"active","currency":"CAD",
                  "quantityAvailable":25,"quantityReserved":2,"price":12.50,"quantitySold":3}]
                """.formatted(ticketId);
        String sessionJson = """
                [{"id":"%s","title":"Opening","startAt":"2026-01-02T03:04:05+00:00",
                  "endAt":null,"timezone":"UTC","sortOrder":1,
                  "location":{"id":"%s","eventId":"%s","name":"Hall","latitude":43.5,
                    "createdAt":"2026-01-01T10:00:00","updatedAt":"2026-01-01T11:00:00","deletedAt":null},
                  "eventTicketTypes":[{"id":"%s","name":"Session ticket","price":8.00}]},
                 {"id":"%s","title":"Online","location":null,"eventTicketTypes":[]}]
                """.formatted(sessionId, locationId, eventId, sessionTicketId, UUID.randomUUID());
        var jdbc = mock(NamedParameterJdbcTemplate.class);
        var adapter = mock(DataSourceAdapter.class);
        var manager = mock(DataSourceManager.class);
        when(manager.get(anyString())).thenReturn(adapter);
        when(adapter.getNamedParameterJdbcTemplate()).thenReturn(jdbc);
        when(jdbc.queryForObject(anyString(), anyMap(), any(RowMapper.class))).thenAnswer(invocation -> {
            String sql = invocation.getArgument(0);
            Map<String, Object> parameters = invocation.getArgument(1);
            RowMapper<EventDetailWithSessionsResponse> mapper = invocation.getArgument(2);
            assertTrue(sql.contains("WHERE ev.id = :eventId"));
            assertTrue(sql.contains("es.\"startAt\"::timestamptz"));
            assertTrue(sql.contains("es.\"endAt\"::timestamptz"));
            assertEquals(Map.of("eventId", eventId), parameters);
            var rs = mock(ResultSet.class);
            when(rs.getObject("id", UUID.class)).thenReturn(eventId);
            when(rs.getString("title")).thenReturn("Event");
            when(rs.getString("eventTicketTypes")).thenReturn(ticketJson);
            when(rs.getString("eventSessions")).thenReturn(sessionJson);
            return mapper.mapRow(rs, 0);
        });

        var details = new EventSessionsDbService(manager).getFullDetails(EventDto.builder().id(eventId).build());

        assertEquals(eventId, details.getId());
        assertEquals("Event", details.getTitle());
        assertEquals(1, details.getEventTicketTypes().size());
        var ticket = details.getEventTicketTypes().getFirst();
        assertEquals(ticketId, ticket.id());
        assertEquals(new BigDecimal("12.50"), ticket.price());
        assertEquals(2, details.getEventSessions().size());
        var session = details.getEventSessions().getFirst();
        assertEquals(sessionId, session.id());
        assertEquals(Instant.parse("2026-01-02T03:04:05Z"), session.startAt());
        assertNull(session.endAt());
        assertEquals(locationId, session.location().id());
        assertEquals(eventId, session.location().eventId());
        assertEquals("Hall", session.location().name());
        assertEquals(LocalDateTime.parse("2026-01-01T10:00:00"), session.location().createdAt());
        assertEquals(sessionTicketId, session.eventTicketTypes().getFirst().id());
        assertEquals(new BigDecimal("8.00"), session.eventTicketTypes().getFirst().price());
        assertNull(details.getEventSessions().get(1).location());
        assertTrue(details.getEventSessions().get(1).eventTicketTypes().isEmpty());
        var responseJson = JsonMapper.builder().build().valueToTree(details);
        assertEquals(eventId.toString(), responseJson.path("id").asString());
        assertEquals(ticketId.toString(), responseJson.path("eventTicketTypes").get(0).path("id").asString());
        assertFalse(responseJson.path("eventTicketTypes").get(0).has("eventSessions"));
        assertEquals(sessionTicketId.toString(), responseJson.path("eventSessions").get(0)
                .path("eventTicketTypes").get(0).path("id").asString());
    }

    @Test
    void mapsEmptyTicketTypes() throws Exception {
        var jdbc = mock(NamedParameterJdbcTemplate.class);
        var adapter = mock(DataSourceAdapter.class);
        var manager = mock(DataSourceManager.class);
        when(manager.get(anyString())).thenReturn(adapter);
        when(adapter.getNamedParameterJdbcTemplate()).thenReturn(jdbc);
        when(jdbc.queryForObject(anyString(), anyMap(), any(RowMapper.class))).thenAnswer(invocation -> {
            RowMapper<EventDetailWithSessionsResponse> mapper = invocation.getArgument(2);
            var rs = mock(ResultSet.class);
            when(rs.getString("eventTicketTypes")).thenReturn("[]");
            when(rs.getString("eventSessions")).thenReturn("[]");
            return mapper.mapRow(rs, 0);
        });

        var details = new EventSessionsDbService(manager)
                .getFullDetails(EventDto.builder().id(UUID.randomUUID()).build());
        assertEquals(0, details.getEventTicketTypes().size());
        assertEquals(0, details.getEventSessions().size());
    }
}
