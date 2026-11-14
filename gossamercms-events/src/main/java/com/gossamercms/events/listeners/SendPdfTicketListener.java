package com.gossamercms.events.listeners;

import com.gossamercms.events.events.EventTicketsCreatedEvent;
import com.gossamercms.events.services.EventOrderService;
import com.gossamercms.media.services.PdfTemplateService;
import com.gossamercms.mvc.annotations.ModuleEventLister;
import com.gossamercms.outbox.dispatcher.GossamerEventListener;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@ModuleEventLister
@RequiredArgsConstructor
public class SendPdfTicketListener implements GossamerEventListener<EventTicketsCreatedEvent> {

    private final PdfTemplateService pdfTemplateService;
    private final EventOrderService eventOrderService;

    @Override
    public Class<EventTicketsCreatedEvent> eventType() {
        return EventTicketsCreatedEvent.class;
    }

    @Override
    public void handle(EventTicketsCreatedEvent event) {
//        Map<String, Object> variables = Map.of(
//                "order", eventOrderService.response(event.getOrder())
//        );
//
//        byte[] pdf = pdfTemplateService.generatePdf(
//                "events/pdf/event-ticket.html",
//                variables
//        );
    }
}
