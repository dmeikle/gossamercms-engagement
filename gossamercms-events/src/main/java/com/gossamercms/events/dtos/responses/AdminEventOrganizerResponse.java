package com.gossamercms.events.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AdminEventOrganizerResponse {
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
}
