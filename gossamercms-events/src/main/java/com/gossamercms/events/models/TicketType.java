package com.gossamercms.events.models;

import com.gossamercms.events.dtos.TicketTypeDto;
import com.gossamercms.mvc.models.BaseModel;
import com.gossamercms.mvc.models.ModelMeta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketType implements BaseModel {

    private UUID id;
    private String name;
    private String description;
    private UUID createdBy;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID updatedBy;


    public static final ModelMeta META = ModelMeta.builderWithId("ticket_types")
            .column("name", String.class, 255)
            .column("description", String.class)
            .column("createdBy", UUID.class)
            .column("createdAt", Instant.class)
            .column("updatedAt", Instant.class)
            .column("updatedBy", UUID.class)
            .defaultSort("id desc")
            .build();

    public TicketTypeDto toDto() {
        return TicketTypeDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .createdBy(createdBy)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .updatedBy(updatedBy)
                .build();
    }

    @Override
    public ModelMeta meta() {
        return null;
    }

    @Override
    public ModelMeta metaOf() {
        return BaseModel.super.metaOf();
    }
}
