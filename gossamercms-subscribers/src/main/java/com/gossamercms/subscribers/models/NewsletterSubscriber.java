package com.gossamercms.subscribers.models;

import com.gossamercms.subscribers.dtos.NewsletterSubscriberDto;
import com.gossamercms.mvc.models.BaseModel;
import com.gossamercms.mvc.models.ModelMeta;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterSubscriber implements BaseModel{
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public NewsletterSubscriberDto toDto() {
        return NewsletterSubscriberDto.builder()
            .id(id)
            .email(email)
            .firstName(firstName)
            .lastName(lastName)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("newsletter_subscribers")
    .column("id", UUID.class)
    .column("email", String.class, 255)
    .column("firstName", String.class, 100)
    .column("lastName", String.class, 100)
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
