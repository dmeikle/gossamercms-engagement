package com.gossamercms.subscribers.models;

import com.gossamercms.subscribers.dtos.NewsletterDto;
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
public class Newsletter implements BaseModel{
    private UUID id;
    private String name;
    private String description;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public NewsletterDto toDto() {
        return NewsletterDto.builder()
            .id(id)
            .name(name)
            .description(description)
            .isActive(isActive)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("newsletters")
    .column("id", UUID.class)
    .column("name", String.class, 150)
    .column("description", String.class, 500)
    .column("isActive", Boolean.class)
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
