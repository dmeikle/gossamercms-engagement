package com.gossamercms.events.data;

import com.gossamercms.events.dtos.responses.EventTicketListResponse;
import com.gossamercms.mvc.data.DataSourceAdapter;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EventTicketsDbServiceTest {
    @Test
    void listsAndCountsOnlyTheUsersRegisteredTickets() {
        var source = new DriverManagerDataSource(
                "jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=PostgreSQL;DB_CLOSE_DELAY=-1", "sa", "");
        var jdbc = new JdbcTemplate(source);
        jdbc.execute("""
                CREATE TABLE events ("id" UUID PRIMARY KEY, title VARCHAR, slug VARCHAR, summary VARCHAR,
                "eventType" VARCHAR, "startAt" TIMESTAMP, "endAt" TIMESTAMP, timezone VARCHAR,
                "thumbnailImageUrl" VARCHAR, "isFeatured" BOOLEAN)
                """);
        jdbc.execute("""
                CREATE TABLE event_registrations ("id" UUID PRIMARY KEY, "eventId" UUID, "userContextId" UUID,
                "status" VARCHAR, "registeredAt" TIMESTAMP, "cancelledAt" TIMESTAMP)
                """);
        jdbc.execute("""
                CREATE TABLE event_tickets ("id" UUID PRIMARY KEY, "registrationId" UUID, "ticketTypeId" UUID,
                "ticketNumber" VARCHAR, "qrTokenHash" VARCHAR, status VARCHAR, "issuedAt" TIMESTAMP)
                """);
        UUID eventId = UUID.randomUUID();
        UUID firstUser = UUID.randomUUID();
        UUID secondUser = UUID.randomUUID();
        UUID firstTicket = UUID.randomUUID();
        UUID secondTicket = UUID.randomUUID();
        jdbc.update("""
                INSERT INTO events ("id", title, "startAt", "isFeatured")
                VALUES (?, 'Example', ?, TRUE)
                """, eventId, Timestamp.from(Instant.parse("2026-01-02T03:04:05Z")));
        insertTicket(jdbc, eventId, firstUser, firstTicket, "REGISTERED");
        insertTicket(jdbc, eventId, firstUser, UUID.randomUUID(), "CANCELLED");
        insertTicket(jdbc, eventId, secondUser, secondTicket, "REGISTERED");

        var namedJdbc = new NamedParameterJdbcTemplate(source);
        var adapter = mock(DataSourceAdapter.class);
        var manager = mock(DataSourceManager.class);
        when(manager.get(anyString())).thenReturn(adapter);
        when(adapter.<EventTicketListResponse>findAllBySql(
                anyString(), anyString(), anyMap(), anyMap(), anyMap(), anyInt(), anyInt(), any()))
                .thenAnswer(invocation -> {
                    String sql = invocation.getArgument(0);
                    String countSql = invocation.getArgument(1);
                    Map<String, Object> filters = invocation.getArgument(2);
                    Map<String, String> columns = invocation.getArgument(3);
                    int page = invocation.getArgument(5);
                    int size = invocation.getArgument(6);
                    RowMapper<EventTicketListResponse> mapper = invocation.getArgument(7);
                    assertEquals("er.userContextId", columns.get("userContextId"));
                    assertEquals("et.status", columns.get("status"));
                    var rows = namedJdbc.query(sql + " LIMIT " + size + " OFFSET " + ((page - 1) * size),
                            filters, mapper);
                    long count = namedJdbc.queryForObject(countSql, filters, Long.class);
                    return ListResultset.of(rows, page, size, count);
                });
        var service = new EventTicketsDbService(manager);

        var first = service.listByUserContext(options(firstUser));
        var second = service.listByUserContext(options(secondUser));

        assertEquals(1, first.totalCount());
        assertEquals(1, first.list().size());
        assertEquals(firstTicket, first.list().getFirst().getTicketId());
        assertEquals(Instant.parse("2026-01-02T03:04:05Z"), first.list().getFirst().getStartAt());
        assertNull(first.list().getFirst().getCancelledAt());
        assertEquals(1, second.totalCount());
        assertEquals(secondTicket, second.list().getFirst().getTicketId());
        assertThrows(IllegalArgumentException.class,
                () -> service.listByUserContext(QueryOptions.builder().build()));
    }

    private static QueryOptions options(UUID userContextId) {
        return QueryOptions.builder().filters(Map.of("userContextId", userContextId)).build();
    }

    private static void insertTicket(JdbcTemplate jdbc, UUID eventId, UUID userId, UUID ticketId, String status) {
        UUID registrationId = UUID.randomUUID();
        jdbc.update("""
                INSERT INTO event_registrations ("id", "eventId", "userContextId", "status")
                VALUES (?, ?, ?, ?)
                """, registrationId, eventId, userId, status);
        jdbc.update("""
                INSERT INTO event_tickets ("id", "registrationId", status) VALUES (?, ?, 'issued')
                """, ticketId, registrationId);
    }
}
