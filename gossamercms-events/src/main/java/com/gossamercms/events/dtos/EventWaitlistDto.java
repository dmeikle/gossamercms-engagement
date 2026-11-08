package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventWaitlist;
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
public class EventWaitlistDto implements DtoWithId{
    private UUID id;
    private UUID eventId;
    private UUID ticketTypeId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer quantityRequested;
    private String status;
    private Integer position;
    private Instant offeredAt;
    private Instant expiresAt;
    private Instant createdAt;
    private Instant updatedAt;

    public EventWaitlist toEntity() {
        return EventWaitlist.builder()
            .id(id)
            .eventId(eventId)
            .ticketTypeId(ticketTypeId)
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .quantityRequested(quantityRequested)
            .status(status)
            .position(position)
            .offeredAt(offeredAt)
            .expiresAt(expiresAt)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
    }
}
