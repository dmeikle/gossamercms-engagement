package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventSession;
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
public class EventSessionDto implements DtoWithId{
    private UUID id;
    private UUID eventId;
    private UUID locationId;
    private String title;
    private String description;
    private Instant startAt;
    private Instant endAt;
    private String timezone;
    private Integer sortOrder;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public EventSession toEntity() {
        return EventSession.builder()
            .id(id)
            .eventId(eventId)
            .locationId(locationId)
            .title(title)
            .description(description)
            .startAt(startAt)
            .endAt(endAt)
            .timezone(timezone)
            .sortOrder(sortOrder)
            .status(status)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
}
