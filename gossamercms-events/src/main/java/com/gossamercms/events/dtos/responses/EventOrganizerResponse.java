package com.gossamercms.events.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventOrganizerResponse {
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
}
