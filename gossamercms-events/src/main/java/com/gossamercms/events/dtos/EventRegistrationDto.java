package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventRegistration;
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
public class EventRegistrationDto implements DtoWithId{
    private UUID id;
    private UUID eventId;
    private UUID orderId;
    private UUID orderItemId;
    private UUID userContextId;
    private String attendeeFirstName;
    private String attendeeLastName;
    private String attendeeEmail;
    private String status;
    private Instant registeredAt;
    private Instant cancelledAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public EventRegistration toEntity() {
        return EventRegistration.builder()
            .id(id)
            .eventId(eventId)
            .orderId(orderId)
            .orderItemId(orderItemId)
            .userContextId(userContextId)
            .attendeeFirstName(attendeeFirstName)
            .attendeeLastName(attendeeLastName)
            .attendeeEmail(attendeeEmail)
            .status(status)
            .registeredAt(registeredAt)
            .cancelledAt(cancelledAt)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
}
