package com.gossamercms.events.services;

import com.gossamercms.events.config.EventCheckoutProperties;
import com.gossamercms.events.data.*;
import com.gossamercms.events.dtos.EventOrderDto;
import com.gossamercms.events.dtos.requests.EventOrderRequest;
import com.gossamercms.events.models.EventTicketType;
import com.gossamercms.events.payments.EventPaymentAdapter;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FreeEventOrdersTest {
    private final EventOrdersDbService orders = mock(EventOrdersDbService.class);
    private final EventOrderItemsDbService items = mock(EventOrderItemsDbService.class);
    private final EventPaymentsDbService payments = mock(EventPaymentsDbService.class);
    private final EventTicketTypesDbService tickets = mock(EventTicketTypesDbService.class);
    private final EventsDbService events = mock(EventsDbService.class);
    private final EventOrderTransactions transactions = mock(EventOrderTransactions.class);
    private final EventOrderFulfillmentService fulfillment = mock(EventOrderFulfillmentService.class);
    @SuppressWarnings("unchecked")
    private final ObjectProvider<EventPaymentAdapter> adapters = mock(ObjectProvider.class);
    private final CurrentJwtUser user = mock(CurrentJwtUser.class);
    private final UUID userContextId = UUID.randomUUID();
    private final UUID eventId = UUID.randomUUID();
    private final UUID ticketId = UUID.randomUUID();
    private final EventCheckoutProperties checkoutProperties = new EventCheckoutProperties();
    private final EventOrderService service = new EventOrderService(orders, items, payments,
            mock(EventRefundsDbService.class), tickets, events, transactions, adapters, checkoutProperties, fulfillment);

    @BeforeEach
    void setUp() {
        when(user.isAuthenticated()).thenReturn(true);
        when(user.getUserContextId()).thenReturn(userContextId);
        when(transactions.transaction(any())).thenAnswer(invocation ->
                invocation.<Supplier<?>>getArgument(0).get());
        when(tickets.ticket(ticketId)).thenReturn(ticket(ticketId, new BigDecimal("0.00")));
        when(items.items(any())).thenReturn(List.of());
        when(orders.order(any(), anyBoolean())).thenAnswer(invocation ->
                EventOrderDto.builder().id(invocation.getArgument(0)).eventId(eventId)
                        .purchaserUserContextId(userContextId).total(BigDecimal.ZERO).status("confirmed").build());
    }

    @Test
    void confirmsFreeOrderWithoutAdapterOrRedirect() {
        var result = service.checkout(user, eventId, request());
        assertNull(result.checkoutUrl());
        assertNotNull(result.accessToken());
        verify(events).requireEventOnSale(eventId);
        verify(tickets).reserveInventory(ticketId, 2);
        verify(tickets).completeReservation(ticketId, 2);
        verify(orders).confirmFreeOrder(any());
        verify(orders).createPublicAccess(any(), anyString(), any());
        verify(items).createLine(any(), any(), eq(ticketId), eq(2), eq(new BigDecimal("0.00")));
        verify(fulfillment).fulfill(argThat(order -> "confirmed".equals(order.getStatus())), eq(List.of()));
        verifyNoInteractions(payments, adapters);
    }

    @Test
    void mixedOrderStillUsesPaymentProvider() {
        checkoutProperties.setSuccessUrl("https://example.com/success");
        checkoutProperties.setCancelUrl("https://example.com/cancel");
        UUID paidId = UUID.randomUUID();
        when(tickets.ticket(paidId)).thenReturn(ticket(paidId, new BigDecimal("10.00")));
        var adapter = mock(EventPaymentAdapter.class);
        when(adapters.getIfUnique()).thenReturn(adapter);
        when(adapter.provider()).thenReturn("test");
        when(adapter.createCheckout(any())).thenReturn(
                new EventPaymentAdapter.Checkout("checkout-1", "https://example.com/checkout"));
        var request = new EventOrderRequest("Buyer", "buyer@example.com", "CAD",
                List.of(new EventOrderRequest.Item(ticketId, 2), new EventOrderRequest.Item(paidId, 1)));
        assertEquals("https://example.com/checkout", service.checkout(user, eventId, request).checkoutUrl());
        verify(payments).createCheckout(any(), any(), eq("test"), eq(new BigDecimal("10.00")), eq("CAD"));
        verify(adapter).createCheckout(argThat(checkout ->
                "https://example.com/success".equals(checkout.successUrl())
                        && "https://example.com/cancel".equals(checkout.cancelUrl())));
        verify(tickets, never()).completeReservation(any(), anyInt());
        verify(orders, never()).confirmFreeOrder(any());
        verifyNoInteractions(fulfillment);
    }

    @Test
    void freeTicketsStillEnforceInventoryAndSalesStatus() {
        var ticket = ticket(ticketId, BigDecimal.ZERO);
        ticket.setQuantityAvailable(1);
        when(tickets.ticket(ticketId)).thenReturn(ticket);
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> service.checkout(user, eventId, request())).getStatusCode());
        ticket.setQuantityAvailable(10);
        ticket.setStatus("PAUSED");
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> service.checkout(user, eventId, request())).getStatusCode());
        verify(orders, never()).confirmFreeOrder(any());
        verifyNoInteractions(payments, adapters);
    }

    @Test
    void negativePricesAreNotFreeAndPaidOrdersRequireRedirectUrls() {
        when(tickets.ticket(ticketId)).thenReturn(ticket(ticketId, new BigDecimal("-1.00")));
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> service.checkout(user, eventId, request())).getStatusCode());
        when(tickets.ticket(ticketId)).thenReturn(ticket(ticketId, new BigDecimal("1.00")));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, assertThrows(ResponseStatusException.class,
                () -> service.checkout(user, eventId, request())).getStatusCode());
        checkoutProperties.setSuccessUrl("javascript:alert(1)");
        checkoutProperties.setCancelUrl("https://example.com/cancel");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, assertThrows(ResponseStatusException.class,
                () -> service.checkout(user, eventId, request())).getStatusCode());
        verify(orders, never()).confirmFreeOrder(any());
        verifyNoInteractions(payments, adapters);
    }

    private EventTicketType ticket(UUID id, BigDecimal price) {
        return EventTicketType.builder().id(id).eventId(eventId).price(price).currency("CAD")
                .status("ON_SALE").isVisible(true).quantityAvailable(10)
                .quantityReserved(0).quantitySold(0).build();
    }

    private EventOrderRequest request() {
        return new EventOrderRequest("Buyer", "buyer@example.com", "CAD",
                List.of(new EventOrderRequest.Item(ticketId, 2)));
    }
}
