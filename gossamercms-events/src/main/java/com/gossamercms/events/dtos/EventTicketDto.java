package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventTicket;
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
public class EventTicketDto implements DtoWithId{
    private UUID id;
    private UUID registrationId;
    private UUID ticketTypeId;
    private String ticketNumber;
    private String qrTokenHash;
    private String status;
    private Instant issuedAt;
    private Instant transferredAt;
    private Instant cancelledAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public EventTicket toEntity() {
        return EventTicket.builder()
            .id(id)
            .registrationId(registrationId)
            .ticketTypeId(ticketTypeId)
            .ticketNumber(ticketNumber)
            .qrTokenHash(qrTokenHash)
            .status(status)
            .issuedAt(issuedAt)
            .transferredAt(transferredAt)
            .cancelledAt(cancelledAt)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
}
