package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventOrganizer;
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
public class EventOrganizerDto implements DtoWithId{
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

    public EventOrganizer toEntity() {
        return EventOrganizer.builder()
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
}
