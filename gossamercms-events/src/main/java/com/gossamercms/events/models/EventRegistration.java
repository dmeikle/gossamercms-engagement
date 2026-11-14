package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventRegistrationDto;
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
public class EventRegistration implements BaseModel{
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

    public EventRegistrationDto toDto() {
        return EventRegistrationDto.builder()
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


public static final ModelMeta META = ModelMeta.builderWithId("event_registrations")
    .column("id", UUID.class)
    .column("eventId", UUID.class)
    .column("orderId", UUID.class)
    .column("orderItemId", UUID.class)
    .column("userContextId", UUID.class)
    .column("attendeeFirstName", String.class, 100)
    .column("attendeeLastName", String.class, 100)
    .column("attendeeEmail", String.class, 255)
    .column("status", String.class)
    .column("registeredAt", Instant.class)
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
