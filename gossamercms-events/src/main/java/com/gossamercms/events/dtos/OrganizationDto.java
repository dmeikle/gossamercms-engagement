package com.gossamercms.events.dtos;

import com.gossamercms.events.models.Organization;
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
public class OrganizationDto implements DtoWithId{
    private UUID id;
    private String name;
    private String slug;
    private String legalName;
    private String description;
    private String email;
    private String phone;
    private String website;
    private String logoUrl;
    private String status;
    private UUID createdBy;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public Organization toEntity() {
        return Organization.builder()
            .id(id)
            .name(name)
            .slug(slug)
            .legalName(legalName)
            .description(description)
            .email(email)
            .phone(phone)
            .website(website)
            .logoUrl(logoUrl)
            .status(status)
            .createdBy(createdBy)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
}
