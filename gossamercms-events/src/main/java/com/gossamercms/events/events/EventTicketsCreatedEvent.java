package com.gossamercms.events.events;

import com.gossamercms.events.dtos.EventOrderDto;
import com.gossamercms.events.dtos.EventTicketDto;
import com.gossamercms.outbox.eventbus.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventTicketsCreatedEvent implements DomainEvent {
    private EventOrderDto order;
    private List<EventTicketDto> tickets;
}
