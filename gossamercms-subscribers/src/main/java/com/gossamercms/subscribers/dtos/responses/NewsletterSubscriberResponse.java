package com.gossamercms.subscribers.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NewsletterSubscriberResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
}
