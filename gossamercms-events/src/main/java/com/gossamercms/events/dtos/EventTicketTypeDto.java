package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventTicketType;
import com.gossamercms.mvc.data.DtoWithId;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventTicketTypeDto implements DtoWithId{
    private UUID id;
    private UUID eventId;
    private UUID sessionId;
    private String name;
    private String description;
    private String ticketType;
    private BigDecimal price;
    private String currency;
    private Integer quantityAvailable;
    private Integer quantityReserved;
    private Integer quantitySold;
    private Integer minimumPerOrder;
    private Integer maximumPerOrder;
    private Instant salesStartAt;
    private Instant salesEndAt;
    private String status;
    private Integer sortOrder;
    private Boolean isVisible;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public EventTicketType toEntity() {
        return EventTicketType.builder()
            .id(id)
            .eventId(eventId)
            .sessionId(sessionId)
            .name(name)
            .description(description)
            .ticketType(ticketType)
            .price(price)
            .currency(currency)
            .quantityAvailable(quantityAvailable)
            .quantityReserved(quantityReserved)
            .quantitySold(quantitySold)
            .minimumPerOrder(minimumPerOrder)
            .maximumPerOrder(maximumPerOrder)
            .salesStartAt(salesStartAt)
            .salesEndAt(salesEndAt)
            .status(status)
            .sortOrder(sortOrder)
            .isVisible(isVisible)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
}
