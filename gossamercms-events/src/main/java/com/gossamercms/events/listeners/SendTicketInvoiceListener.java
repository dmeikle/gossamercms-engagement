package com.gossamercms.events.listeners;

import com.gossamercms.events.events.EventTicketsCreatedEvent;
import com.gossamercms.mvc.annotations.ModuleEventLister;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import com.gossamercms.notifications.dtos.MessageAudienceDto;
import com.gossamercms.notifications.dtos.requests.CreateMessageRequest;
import com.gossamercms.notifications.enums.MessageAudienceType;
import com.gossamercms.notifications.services.MessagesService;
import com.gossamercms.outbox.dispatcher.GossamerEventListener;
import com.gossamercms.security.jwt.JwtUser;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@ModuleEventLister
@RequiredArgsConstructor
public class SendTicketInvoiceListener implements GossamerEventListener<EventTicketsCreatedEvent> {

    private final MessagesService messagesService;

    @Override
    public Class<EventTicketsCreatedEvent> eventType() {
        return EventTicketsCreatedEvent.class;
    }

    @Override
    public void handle(EventTicketsCreatedEvent event) {
        CreateMessageRequest request = new CreateMessageRequest();

        request.setEventKey("event.registration.completed");

        MessageAudienceDto audience = MessageAudienceDto.builder()
                .type(MessageAudienceType.USER)
                .userContextId(event.getOrder().getPurchaserUserContextId())
                .build();

        request.setAudiences(List.of(audience));

        request.setLocale("en-CA");

//        request.setParameters(Map.of(
//                "articleTitle", article.getTitle(),
//                "articleId", article.getId(),
//                "authorName", author.getName()
//        ));
        request.setParameters(Map.of(
                "order", event.getOrder(),
                "items", event.getTickets()
        ));

        messagesService.send(new JwtUser(event.getOrder().getPurchaserUserContextId(),//we aren't using this value
                event.getOrder().getPurchaserUserContextId()), request);;
    }
}
