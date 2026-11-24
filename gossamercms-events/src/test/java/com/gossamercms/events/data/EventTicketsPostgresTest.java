package com.gossamercms.events.data;

import com.gossamercms.mvc.data.DataSourceAdapter;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.QueryOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@EnabledIfEnvironmentVariable(named = "EVENTS_TEST_POSTGRES_URL", matches = ".+")
class EventTicketsPostgresTest {
    @Test
    void groupsTicketsAndCountsEventsForTheRequestedUser() {
        String url = System.getenv("EVENTS_TEST_POSTGRES_URL");
        String user = System.getenv("EVENTS_TEST_POSTGRES_USER");
        String password = System.getenv("EVENTS_TEST_POSTGRES_PASSWORD");
        String schema = "ticket_test_" + UUID.randomUUID().toString().replace("-", "");
        var admin = new JdbcTemplate(new DriverManagerDataSource(url, user, password));
        admin.execute("CREATE SCHEMA " + schema);
        try {
            String scopedUrl = url + (url.contains("?") ? "&" : "?") + "currentSchema=" + schema;
            var source = new DriverManagerDataSource(scopedUrl, user, password);
            var jdbc = new JdbcTemplate(source);
            jdbc.execute("""
                    CREATE TABLE events (id UUID PRIMARY KEY, title TEXT, slug TEXT, summary TEXT,
                    "eventType" TEXT, "startAt" TIMESTAMP, "endAt" TIMESTAMP, timezone TEXT,
                    "thumbnailImageUrl" TEXT, "isFeatured" BOOLEAN)
                    """);
            jdbc.execute("""
                    CREATE TABLE event_orders (id UUID PRIMARY KEY, "orderNumber" TEXT,
                    total DECIMAL, currency TEXT, "paidAt" TIMESTAMP)
                    """);
            jdbc.execute("""
                    CREATE TABLE event_registrations (id UUID PRIMARY KEY, "eventId" UUID, "orderId" UUID,
                    "userContextId" UUID, status TEXT, "registeredAt" TIMESTAMP, "cancelledAt" TIMESTAMP)
                    """);
            jdbc.execute("""
                    CREATE TABLE event_tickets (id UUID PRIMARY KEY, "registrationId" UUID, "ticketTypeId" UUID,
                    "ticketNumber" TEXT, "qrTokenHash" TEXT, status TEXT, "issuedAt" TIMESTAMP)
                    """);
            jdbc.execute("""
                    CREATE TABLE event_ticket_types (id UUID PRIMARY KEY, "ticketTypeId" UUID,
                    "sessionId" UUID, name TEXT)
                    """);
            jdbc.execute("CREATE TABLE ticket_types (id UUID PRIMARY KEY, name TEXT)");
            jdbc.execute("""
                    CREATE TABLE event_sessions (id UUID PRIMARY KEY, "eventId" UUID, title TEXT,
                    "startAt" TIMESTAMP, "endAt" TIMESTAMP, timezone TEXT, "sortOrder" INTEGER, "locationId" UUID)
                    """);
            jdbc.execute("""
                    CREATE TABLE event_locations (id UUID PRIMARY KEY, "eventId" UUID, name TEXT,
                    "createdAt" TIMESTAMP)
                    """);
            UUID firstUser = UUID.randomUUID();
            UUID otherUser = UUID.randomUUID();
            UUID firstEvent = UUID.randomUUID();
            UUID secondEvent = UUID.randomUUID();
            UUID session = UUID.randomUUID();
            UUID location = UUID.randomUUID();
            jdbc.update("INSERT INTO events (id, title, \"startAt\") VALUES (?, 'First', '2026-01-01'), (?, 'Second', '2026-02-01')",
                    firstEvent, secondEvent);
            jdbc.update("INSERT INTO event_locations (id, \"eventId\", name) VALUES (?, ?, 'Hall')", location, firstEvent);
            jdbc.update("""
                    INSERT INTO event_sessions (id, "eventId", title, "startAt", "locationId")
                    VALUES (?, ?, 'Session', '2026-01-02 12:00:00', ?)
                    """, session, firstEvent, location);
            UUID firstTicket = insertTicket(jdbc, firstUser, firstEvent, null, "REGISTERED", "issued");
            UUID sessionTicket = insertTicket(jdbc, firstUser, firstEvent, session, "REGISTERED", "issued");
            insertTicket(jdbc, firstUser, secondEvent, null, "REGISTERED", "issued");
            insertTicket(jdbc, otherUser, firstEvent, null, "REGISTERED", "issued");
            insertTicket(jdbc, firstUser, firstEvent, null, "CANCELLED", "issued");
            insertTicket(jdbc, firstUser, firstEvent, null, "REGISTERED", "cancelled");
            var adapter = mock(DataSourceAdapter.class);
            var manager = mock(DataSourceManager.class);
            when(manager.get(anyString())).thenReturn(adapter);
            when(adapter.getNamedParameterJdbcTemplate()).thenReturn(new NamedParameterJdbcTemplate(source));
            var service = new EventTicketsDbService(manager);
            var options = QueryOptions.builder().page(1).size(1)
                    .filters(Map.of("userContextId", firstUser, "status", "issued")).build();

            var result = service.listByUserContext(options);
            assertEquals(2, result.totalCount());
            assertEquals(2, result.totalPages());
            assertEquals(firstEvent, result.list().getFirst().getEventId());
            assertEquals(firstTicket, result.list().getFirst().getTickets().getFirst().ticketId());
            assertEquals("Admission", result.list().getFirst().getTickets().getFirst().ticketTypeName());
            assertEquals(sessionTicket, result.list().getFirst().getSessions().getFirst().tickets().getFirst().ticketId());
            assertEquals(location, result.list().getFirst().getSessions().getFirst().location().id());
            assertNotNull(result.list().getFirst().getSessions().getFirst().startAt());
            var next = service.listByUserContext(QueryOptions.builder().page(2).size(1)
                    .filters(Map.of("userContextId", firstUser, "status", "issued")).build());
            assertEquals(secondEvent, next.list().getFirst().getEventId());
            assertTrue(next.list().getFirst().getSessions().isEmpty());
            assertEquals(2, next.totalCount());
            var other = service.listByUserContext(QueryOptions.builder()
                    .filters(Map.of("userContextId", otherUser)).build());
            assertEquals(1, other.totalCount());
            assertEquals(1, other.list().getFirst().getTickets().size());
            assertTrue(other.list().getFirst().getSessions().isEmpty());
            var filtered = service.listByUserContext(QueryOptions.builder()
                    .filters(Map.of("userContextId", firstUser, "status", "issued",
                            "eventId", secondEvent.toString())).build());
            assertEquals(1, filtered.totalCount());
            assertEquals(secondEvent, filtered.list().getFirst().getEventId());
        } finally {
            admin.execute("DROP SCHEMA " + schema + " CASCADE");
        }
    }

    private static UUID insertTicket(JdbcTemplate jdbc, UUID user, UUID event, UUID session,
                                     String registrationStatus, String ticketStatus) {
        UUID order = UUID.randomUUID();
        UUID registration = UUID.randomUUID();
        UUID ticketType = UUID.randomUUID();
        UUID eventTicketType = UUID.randomUUID();
        UUID ticket = UUID.randomUUID();
        jdbc.update("INSERT INTO event_orders (id, \"orderNumber\", total, currency) VALUES (?, 'ABC', 25.50, 'CAD')", order);
        jdbc.update("INSERT INTO ticket_types (id, name) VALUES (?, 'Admission')", ticketType);
        jdbc.update("INSERT INTO event_ticket_types (id, \"ticketTypeId\", \"sessionId\", name) VALUES (?, ?, ?, 'General')",
                eventTicketType, ticketType, session);
        jdbc.update("""
                INSERT INTO event_registrations (id, "eventId", "orderId", "userContextId", status)
                VALUES (?, ?, ?, ?, ?)
                """, registration, event, order, user, registrationStatus);
        jdbc.update("""
                INSERT INTO event_tickets (id, "registrationId", "ticketTypeId", status, "issuedAt")
                VALUES (?, ?, ?, ?, '2026-01-01 10:00:00')
                """, ticket, registration, eventTicketType, ticketStatus);
        return ticket;
    }
}
