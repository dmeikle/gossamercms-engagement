package com.gossamercms.events.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEventOrganizerRequest {
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
