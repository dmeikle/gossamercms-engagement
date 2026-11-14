package com.gossamercms.events.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class AdminEventTicketTypeResponse {
    private UUID id;
    private UUID eventId;
    private UUID sessionId;
    private String name;
    private String description;
    private UUID ticketTypeId;
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
}
