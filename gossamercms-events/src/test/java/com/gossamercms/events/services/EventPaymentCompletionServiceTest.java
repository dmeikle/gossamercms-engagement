package com.gossamercms.events.services;

import com.gossamercms.events.data.*;
import com.gossamercms.mvc.data.DataSourceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventPaymentCompletionServiceTest {
    private final UUID order = UUID.randomUUID();
    private final UUID payment = UUID.randomUUID();
    private final UUID ticket = UUID.randomUUID();
    private JdbcTemplate jdbc;
    private EventPaymentCompletionService service;
    private EventOrderFulfillmentService fulfillment;

    @BeforeEach
    void setUp() {
        var source = new DriverManagerDataSource(
                "jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=PostgreSQL;DB_CLOSE_DELAY=-1", "sa", "");
        jdbc = new JdbcTemplate(source);
        jdbc.execute("""
                CREATE TABLE event_orders ("id" UUID PRIMARY KEY, "total" DECIMAL, "currency" VARCHAR,
                "status" VARCHAR, "expiresAt" TIMESTAMP, "paidAt" TIMESTAMP, "cancelledAt" TIMESTAMP,
                "updatedAt" TIMESTAMP, "deletedAt" TIMESTAMP)
                """);
        jdbc.execute("""
                CREATE TABLE event_payments ("id" UUID PRIMARY KEY, "orderId" UUID, "provider" VARCHAR,
                "providerCheckoutId" VARCHAR, "providerPaymentId" VARCHAR, "amount" DECIMAL,
                "currency" VARCHAR, "status" VARCHAR, "processedAt" TIMESTAMP, "updatedAt" TIMESTAMP)
                """);
        jdbc.execute("""
                CREATE TABLE event_order_items ("id" UUID PRIMARY KEY, "orderId" UUID, "ticketTypeId" UUID,
                "quantity" INT, "unitPrice" DECIMAL, "lineTotal" DECIMAL)
                """);
        jdbc.execute("""
                CREATE TABLE event_ticket_types ("id" UUID PRIMARY KEY, "quantityReserved" INT,
                "quantitySold" INT, "updatedAt" TIMESTAMP)
                """);
        jdbc.update("INSERT INTO event_orders (\"id\",\"total\",\"currency\",\"status\") VALUES (?,20,'CAD','pending')", order);
        jdbc.update("""
                INSERT INTO event_payments ("id","orderId","provider","providerCheckoutId","amount","currency","status")
                VALUES (?,?,'stripe-mock','session',20,'CAD','pending')
                """, payment, order);
        jdbc.update("""
                INSERT INTO event_order_items VALUES (?,?,?,2,10,20)
                """, UUID.randomUUID(), order, ticket);
        jdbc.update("INSERT INTO event_ticket_types (\"id\",\"quantityReserved\",\"quantitySold\") VALUES (?,2,0)", ticket);
        var manager = mock(DataSourceManager.class, RETURNS_DEEP_STUBS);
        when(manager.get(anyString()).getNamedParameterJdbcTemplate())
                .thenReturn(new NamedParameterJdbcTemplate(source));
        fulfillment = mock(EventOrderFulfillmentService.class);
        service = new EventPaymentCompletionService(new EventOrderTransactions(manager),
                new EventOrdersDbService(manager), new EventPaymentsDbService(manager),
                new EventOrderItemsDbService(manager), new EventTicketTypesDbService(manager), fulfillment);
    }

    @Test
    void paymentIsAtomicAndDuplicateNotificationsDoNotSellTwice() {
        complete(false, new BigDecimal("20.00"));
        complete(false, new BigDecimal("20.00"));
        assertEquals("paid", value("event_orders", "status"));
        assertEquals("paid", value("event_payments", "status"));
        assertEquals("mock-payment", value("event_payments", "providerPaymentId"));
        assertNotNull(value("event_orders", "paidAt"));
        assertEquals(0, value("event_ticket_types", "quantityReserved"));
        assertEquals(2, value("event_ticket_types", "quantitySold"));
        verify(fulfillment, times(1)).fulfill(argThat(paidOrder ->
                order.equals(paidOrder.getId()) && "paid".equals(paidOrder.getStatus())),
                argThat(lines -> lines.size() == 1 && ticket.equals(lines.get(0).ticketTypeId())
                        && lines.get(0).quantity() == 2));
        assertThrows(ResponseStatusException.class, () -> complete(true, new BigDecimal("20")));
    }

    @Test
    void cancellationReleasesInventoryOnlyOnce() {
        complete(true, new BigDecimal("20"));
        complete(true, new BigDecimal("20"));
        assertEquals("cancelled", value("event_orders", "status"));
        assertEquals("cancelled", value("event_payments", "status"));
        assertEquals(0, value("event_ticket_types", "quantityReserved"));
        assertEquals(0, value("event_ticket_types", "quantitySold"));
        assertThrows(ResponseStatusException.class, () -> complete(false, new BigDecimal("20")));
        verifyNoInteractions(fulfillment);
    }

    @Test
    void fulfillmentFailureRollsBackPaymentAndInventory() {
        doThrow(new IllegalStateException("fulfillment failed")).when(fulfillment).fulfill(any(), anyList());

        assertThrows(IllegalStateException.class, () -> complete(false, new BigDecimal("20")));
        assertEquals("pending", value("event_orders", "status"));
        assertEquals("pending", value("event_payments", "status"));
        assertEquals(2, value("event_ticket_types", "quantityReserved"));
        assertEquals(0, value("event_ticket_types", "quantitySold"));
    }

    @Test
    void mismatchesAndInventoryFailureLeaveOrderUnpaid() {
        assertThrows(ResponseStatusException.class, () -> complete(false, BigDecimal.ONE));
        jdbc.update("UPDATE event_ticket_types SET \"quantityReserved\"=0");
        assertThrows(ResponseStatusException.class, () -> complete(false, new BigDecimal("20")));
        assertEquals("pending", value("event_orders", "status"));
        assertEquals("pending", value("event_payments", "status"));
    }

    @Test
    void expiredOrderCannotBePaid() {
        jdbc.update("UPDATE event_orders SET \"expiresAt\"=TIMESTAMP '2000-01-01 00:00:00'");
        assertThrows(ResponseStatusException.class, () -> complete(false, new BigDecimal("20")));
        assertEquals(2, value("event_ticket_types", "quantityReserved"));
    }

    @Test
    void failureOnLaterTicketRollsBackEarlierInventoryChanges() {
        UUID first = UUID.fromString("00000000-0000-4000-8000-000000000001");
        UUID second = UUID.fromString("00000000-0000-4000-8000-000000000002");
        jdbc.update("DELETE FROM event_order_items");
        jdbc.update("DELETE FROM event_ticket_types");
        jdbc.update("INSERT INTO event_ticket_types (\"id\",\"quantityReserved\",\"quantitySold\") VALUES (?,2,0)", first);
        jdbc.update("INSERT INTO event_ticket_types (\"id\",\"quantityReserved\",\"quantitySold\") VALUES (?,0,0)", second);
        for (UUID id : java.util.List.of(first, second))
            jdbc.update("INSERT INTO event_order_items VALUES (?,?,?,2,5,10)", UUID.randomUUID(), order, id);
        assertThrows(ResponseStatusException.class, () -> complete(false, new BigDecimal("20")));
        assertEquals(2, jdbc.queryForObject(
                "SELECT \"quantityReserved\" FROM event_ticket_types WHERE \"id\"=?", Integer.class, first));
        assertEquals(0, jdbc.queryForObject(
                "SELECT \"quantitySold\" FROM event_ticket_types WHERE \"id\"=?", Integer.class, first));
        assertEquals("pending", value("event_orders", "status"));
    }

    @Test
    void mismatchedProviderSessionAndCurrencyAndCancellationClaimsAreRejected() {
        assertThrows(ResponseStatusException.class, () ->
                service.complete(order, "stripe", "session", "payment", new BigDecimal("20"), "CAD", false));
        assertThrows(ResponseStatusException.class, () ->
                service.complete(order, "stripe-mock", "wrong", "payment", new BigDecimal("20"), "CAD", false));
        assertThrows(ResponseStatusException.class, () ->
                service.complete(order, "stripe-mock", "session", "payment", new BigDecimal("20"), "USD", false));
        jdbc.update("UPDATE event_orders SET \"status\"='cancelling'");
        assertThrows(ResponseStatusException.class, () -> complete(false, new BigDecimal("20")));
        assertEquals(2, value("event_ticket_types", "quantityReserved"));
    }

    private void complete(boolean cancel, BigDecimal amount) {
        service.complete(order, "stripe-mock", "session", "mock-payment", amount, "CAD", cancel);
    }

    private Object value(String table, String column) {
        return jdbc.queryForObject("SELECT \"" + column + "\" FROM " + table, Object.class);
    }
}
