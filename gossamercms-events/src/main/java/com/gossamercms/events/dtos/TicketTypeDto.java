package com.gossamercms.events.dtos;

import com.gossamercms.events.models.TicketType;
import com.gossamercms.mvc.data.DtoWithId;
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
public class TicketTypeDto implements DtoWithId {
    private UUID id;
    private String name;
    private String description;
    private UUID createdBy;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID updatedBy;

    public TicketType toEntity() {
        return TicketType.builder()
                .id(id)
                .name(name)
                .description(description)
                .createdBy(createdBy)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .updatedBy(updatedBy)
                .build();
    }
}
