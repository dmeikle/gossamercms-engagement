package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventOrganizerDto;
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
public class EventOrganizer implements BaseModel{
    private UUID id;
    private UUID organizationId;
    private String name;
    private String slug;
    private String description;
    private String email;
    private String phone;
    private String website;
    private String logoUrl;
    private String status;
    private String stripeAccountId;
    private Boolean stripeChargesEnabled;
    private Boolean stripePayoutsEnabled;
    private Boolean stripeDetailsSubmitted;
    private UUID createdBy;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public EventOrganizerDto toDto() {
        return EventOrganizerDto.builder()
            .id(id)
            .organizationId(organizationId)
            .name(name)
            .slug(slug)
            .description(description)
            .email(email)
            .phone(phone)
            .website(website)
            .logoUrl(logoUrl)
            .status(status)
            .stripeAccountId(stripeAccountId)
            .stripeChargesEnabled(stripeChargesEnabled)
            .stripePayoutsEnabled(stripePayoutsEnabled)
            .stripeDetailsSubmitted(stripeDetailsSubmitted)
            .createdBy(createdBy)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("event_organizers")
    .column("id", UUID.class)
    .column("organizationId", UUID.class)
    .column("name", String.class, 255)
    .column("slug", String.class, 255)
    .column("description", String.class)
    .column("email", String.class, 255)
    .column("phone", String.class, 50)
    .column("website", String.class, 500)
    .column("logoUrl", String.class, 1000)
    .column("status", String.class)
    .column("stripeAccountId", String.class, 255)
    .column("stripeChargesEnabled", Boolean.class)
    .column("stripePayoutsEnabled", Boolean.class)
    .column("stripeDetailsSubmitted", Boolean.class)
    .column("createdBy", UUID.class)
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
