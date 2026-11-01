package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventTicketTypeDto;
import com.gossamercms.mvc.models.BaseModel;
import com.gossamercms.mvc.models.ModelMeta;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventTicketType implements BaseModel{
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

    public EventTicketTypeDto toDto() {
        return EventTicketTypeDto.builder()
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


public static final ModelMeta META = ModelMeta.builderWithId("event_ticket_types")
    .column("id", UUID.class)
    .column("eventId", UUID.class)
    .column("sessionId", UUID.class)
    .column("name", String.class, 255)
    .column("description", String.class)
    .column("ticketType", String.class)
    .column("price", BigDecimal.class)
    .column("currency", String.class, 3)
    .column("quantityAvailable", Integer.class)
    .column("quantityReserved", Integer.class)
    .column("quantitySold", Integer.class)
    .column("minimumPerOrder", Integer.class)
    .column("maximumPerOrder", Integer.class)
    .column("salesStartAt", Instant.class)
    .column("salesEndAt", Instant.class)
    .column("status", String.class)
    .column("sortOrder", Integer.class)
    .column("isVisible", Boolean.class)
    .column("createdAt", Instant.class)
    .column("updatedAt", Instant.class)
    .column("deletedAt", Instant.class)
    .defaultSort("id desc")
    .build();



    @Override
    public ModelMeta meta() {
        return META;
    }

    @Override
    public ModelMeta metaOf() {
        return BaseModel.super.metaOf();
    }

}
