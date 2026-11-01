package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventWaitlistDto;
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
public class EventWaitlist implements BaseModel{
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

    public EventWaitlistDto toDto() {
        return EventWaitlistDto.builder()
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


public static final ModelMeta META = ModelMeta.builderWithId("event_waitlists")
    .column("id", UUID.class)
    .column("eventId", UUID.class)
    .column("ticketTypeId", UUID.class)
    .column("firstName", String.class, 100)
    .column("lastName", String.class, 100)
    .column("email", String.class, 255)
    .column("quantityRequested", Integer.class)
    .column("status", String.class)
    .column("position", Integer.class)
    .column("offeredAt", Instant.class)
    .column("expiresAt", Instant.class)
    .column("createdAt", Instant.class)
    .column("updatedAt", Instant.class)
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
