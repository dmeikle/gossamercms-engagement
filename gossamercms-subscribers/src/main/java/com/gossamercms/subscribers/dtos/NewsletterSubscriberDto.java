package com.gossamercms.subscribers.dtos;

import com.gossamercms.subscribers.models.NewsletterSubscriber;
import com.gossamercms.mvc.data.DtoWithId;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterSubscriberDto implements DtoWithId{
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public NewsletterSubscriber toEntity() {
        return NewsletterSubscriber.builder()
            .id(id)
            .email(email)
            .firstName(firstName)
            .lastName(lastName)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
}
