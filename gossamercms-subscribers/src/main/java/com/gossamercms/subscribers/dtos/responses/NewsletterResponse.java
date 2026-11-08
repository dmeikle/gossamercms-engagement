package com.gossamercms.subscribers.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NewsletterResponse {
    private UUID id;
    private String name;
    private String description;
    private Boolean isActive;
}
