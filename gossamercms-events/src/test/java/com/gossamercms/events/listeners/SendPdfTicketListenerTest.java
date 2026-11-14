package com.gossamercms.events.listeners;

import com.gossamercms.events.dtos.EventOrderDto;
import com.gossamercms.events.dtos.responses.EventOrderResponse;
import com.gossamercms.events.events.EventTicketsCreatedEvent;
import com.gossamercms.events.services.EventOrderService;
import com.gossamercms.media.services.PdfTemplateService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SendPdfTicketListenerTest {
    @Test
    @Disabled("PDF ticket generation is temporarily disabled in SendPdfTicketListener")
    void suppliesTheOrderResponseWithCustomerAndLineItemFields() {
        PdfTemplateService pdf = mock(PdfTemplateService.class);
        EventOrderService orders = mock(EventOrderService.class);
        EventOrderDto order = EventOrderDto.builder()
                .id(UUID.randomUUID())
                .eventId(UUID.randomUUID())
                .orderNumber("EO-123")
                .purchaserName("Test Customer")
                .purchaserEmail("customer@example.test")
                .currency("CAD")
                .subtotal(new BigDecimal("50.00"))
                .total(new BigDecimal("50.00"))
                .status("confirmed")
                .paidAt(Instant.parse("2026-11-12T12:00:00Z"))
                .build();
        EventOrderResponse.Item item = new EventOrderResponse.Item(
                UUID.randomUUID(), UUID.randomUUID(), 2,
                new BigDecimal("25.00"), new BigDecimal("50.00"));
        EventOrderResponse response = EventOrderResponse.from(order, List.of(item));
        when(orders.response(order)).thenReturn(response);

        new SendPdfTicketListener(pdf, orders).handle(
                EventTicketsCreatedEvent.builder().order(order).tickets(List.of()).build());

        verify(pdf).generatePdf("events/pdf/event-ticket.html", Map.of("order", response));
        assertEquals("Test Customer", response.name());
        assertEquals("customer@example.test", response.email());
        assertEquals(List.of(item), response.items());
    }
}
