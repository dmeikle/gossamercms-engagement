package com.gossamercms.events.data;

import com.gossamercms.events.dtos.responses.EventTicketListResponse;
import com.gossamercms.events.dtos.responses.EventTicketDetailResponse;
import com.gossamercms.mvc.data.DataSourceAdapter;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EventTicketsDbServiceTest {
    @Test
    void mapsDetailFilterToTicketIdColumn() {
        UUID ticketId = UUID.randomUUID();
        var adapter = mock(DataSourceAdapter.class);
        var manager = mock(DataSourceManager.class);
        when(manager.get(anyString())).thenReturn(adapter);
        var expected = ListResultset.of(List.of(EventTicketDetailResponse.builder().ticketId(ticketId).build()), 1, 1, 1);
        when(adapter.<EventTicketDetailResponse>findAllBySql(anyString(), anyString(), anyMap(),
                anyMap(), isNull(), eq(1), eq(1), any())).thenReturn(expected);

        assertSame(expected, new EventTicketsDbService(manager).getTicketDetails(ticketId));
        verify(adapter).findAllBySql(
                contains("where et.id = :eventTicketId"),
                contains("where et.id = :eventTicketId"),
                eq(Map.of("eventTicketId", ticketId)),
                eq(Map.of("eventTicketId", "et.id")),
                isNull(), eq(1), eq(1), any());
    }

    @Test
    void mapsEventLevelAndSessionTicketsWithPagination() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID ticketId = UUID.randomUUID();
        UUID sessionTicketId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        UUID locationId = UUID.randomUUID();
        String tickets = """
                [{"ticketId":"%s","orderNumber":"ABC","total":25.50,"currency":"CAD",
                  "paidAt":"2026-01-02T03:04:05+00:00","eventTicketTypeName":"General",
                  "ticketTypeName":"Admission","registrationId":"%s","registeredAt":null,
                  "cancelledAt":null,"eventId":"%s","ticketTypeId":"%s","ticketNumber":"1",
                  "qrTokenHash":"hash","status":"issued","issuedAt":"2026-01-02T03:04:05+00:00"}]
                """.formatted(ticketId, UUID.randomUUID(), eventId, UUID.randomUUID());
        String sessions = """
                [{"id":"%s","title":"Opening","startAt":"2026-01-02T04:00:00+00:00","endAt":null,
                  "timezone":"UTC","sortOrder":1,
                  "location":{"id":"%s","eventId":"%s","name":"Hall","createdAt":"2026-01-01T10:00:00"},
                  "tickets":[{"ticketId":"%s","eventId":"%s","total":10.00}]},
                 {"id":"%s","title":"Online","location":null,"tickets":[]}]
                """.formatted(sessionId, locationId, eventId, sessionTicketId, eventId, UUID.randomUUID());
        var jdbc = mock(NamedParameterJdbcTemplate.class);
        var adapter = mock(DataSourceAdapter.class);
        var manager = mock(DataSourceManager.class);
        when(manager.get(anyString())).thenReturn(adapter);
        when(adapter.getNamedParameterJdbcTemplate()).thenReturn(jdbc);
        when(jdbc.query(anyString(), anyMap(), any(RowMapper.class))).thenAnswer(invocation -> {
            String sql = invocation.getArgument(0);
            Map<String, Object> parameters = invocation.getArgument(1);
            RowMapper<EventTicketListResponse> mapper = invocation.getArgument(2);
            assertTrue(sql.contains("er.\"userContextId\" = :userContextId"));
            assertTrue(sql.contains("et.status = :status"));
            assertEquals(userId, parameters.get("userContextId"));
            assertEquals("issued", parameters.get("status"));
            assertEquals(1L, parameters.get("offset"));
            assertEquals(1, parameters.get("limit"));
            ResultSet rs = mock(ResultSet.class);
            when(rs.getObject("eventId", UUID.class)).thenReturn(eventId);
            when(rs.getString("title")).thenReturn("Event");
            when(rs.getTimestamp("startAt")).thenReturn(Timestamp.from(Instant.parse("2026-01-02T03:04:05Z")));
            when(rs.getString("tickets")).thenReturn(tickets);
            when(rs.getString("sessions")).thenReturn(sessions);
            return List.of(mapper.mapRow(rs, 0));
        });
        when(jdbc.queryForObject(anyString(), anyMap(), eq(Long.class))).thenAnswer(invocation -> {
            String sql = invocation.getArgument(0);
            Map<String, Object> parameters = invocation.getArgument(1);
            assertTrue(sql.contains("SELECT count(*) FROM events e"));
            assertTrue(sql.contains("et.status = :status"));
            assertEquals(userId, parameters.get("userContextId"));
            return 2L;
        });
        var service = new EventTicketsDbService(manager);

        var result = service.listByUserContext(QueryOptions.builder().page(2).size(1)
                .filters(Map.of("userContextId", userId, "status", "issued")).build());

        assertEquals(2, result.totalCount());
        assertEquals(2, result.page());
        var event = result.list().getFirst();
        assertEquals(eventId, event.getEventId());
        assertEquals(Instant.parse("2026-01-02T03:04:05Z"), event.getStartAt());
        assertNull(event.getEndAt());
        assertEquals(ticketId, event.getTickets().getFirst().ticketId());
        assertEquals(new BigDecimal("25.50"), event.getTickets().getFirst().total());
        assertEquals("Admission", event.getTickets().getFirst().ticketTypeName());
        assertEquals("hash", event.getTickets().getFirst().qrTokenHash());
        assertEquals(Instant.parse("2026-01-02T03:04:05Z"), event.getTickets().getFirst().paidAt());
        assertNull(event.getTickets().getFirst().cancelledAt());
        var session = event.getSessions().getFirst();
        assertEquals(sessionId, session.id());
        assertEquals(locationId, session.location().id());
        assertEquals(LocalDateTime.parse("2026-01-01T10:00:00"), session.location().createdAt());
        assertEquals(sessionTicketId, session.tickets().getFirst().ticketId());
        assertNull(event.getSessions().get(1).location());
        assertTrue(event.getSessions().get(1).tickets().isEmpty());
    }

    @Test
    void rejectsMissingUserAndUnsupportedFilters() {
        var service = new EventTicketsDbService(mock(DataSourceManager.class));
        assertThrows(IllegalArgumentException.class,
                () -> service.listByUserContext(QueryOptions.builder().build()));
        assertThrows(ResponseStatusException.class, () -> service.listByUserContext(QueryOptions.builder()
                .filters(Map.of("userContextId", UUID.randomUUID(), "unexpected", "value")).build()));
        assertThrows(ResponseStatusException.class, () -> service.listByUserContext(QueryOptions.builder()
                .filters(Map.of("userContextId", UUID.randomUUID(), "eventId", "invalid")).build()));
    }
}
