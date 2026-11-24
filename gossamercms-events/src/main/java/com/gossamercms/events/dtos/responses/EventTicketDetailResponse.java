package com.gossamercms.events.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventTicketDetailResponse {
    private UUID ticketId;
    private UUID eventId;
    private UUID sessionId;
    private UUID ticketTypeId;
    private String ticketNumber;
    private String status;
    private UUID orderId;
    private String orderNumber;
    private String orderStatus;
    private String ticketTypeName;
    private BigDecimal price;
    private String sessionTitle;
    private String sessionDescription;
    private Instant sessionStartAt;
    private Instant sessionEndAt;
    private String eventTitle;
    private String eventDescription;
    private String eventSummary;
    private Instant eventStartAt;
    private Instant eventEndAt;
    private String attendeeEmail;
    private String attendeeFirstName;
    private String attendeeLastName;
}