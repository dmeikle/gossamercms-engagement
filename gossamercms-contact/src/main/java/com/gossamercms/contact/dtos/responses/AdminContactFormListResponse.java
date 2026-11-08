package com.gossamercms.contact.dtos.responses;

import com.gossamercms.mvc.helpers.annotations.JsonColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminContactFormListResponse {
    private UUID id;
    private String name;
    private String slug;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
