package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventTicketDto;
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
public class EventTicket implements BaseModel{
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

    public EventTicketDto toDto() {
        return EventTicketDto.builder()
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


public static final ModelMeta META = ModelMeta.builderWithId("event_tickets")
    .column("id", UUID.class)
    .column("registrationId", UUID.class)
    .column("ticketTypeId", UUID.class)
    .column("ticketNumber", String.class, 100)
    .column("qrTokenHash", String.class, 255)
    .column("status", String.class)
    .column("issuedAt", Instant.class)
    .column("transferredAt", Instant.class)
    .column("cancelledAt", Instant.class)
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
