package com.gossamercms.events.dtos;

import com.gossamercms.events.models.EventCheckIn;
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
public class EventCheckInDto implements DtoWithId{
    private UUID id;
    private UUID ticketId;
    private UUID sessionId;
    private UUID checkedInBy;
    private Instant checkedInAt;
    private String method;
    private Instant createdAt;

    public EventCheckIn toEntity() {
        return EventCheckIn.builder()
            .id(id)
            .ticketId(ticketId)
            .sessionId(sessionId)
            .checkedInBy(checkedInBy)
            .checkedInAt(checkedInAt)
            .method(method)
            .createdAt(createdAt)
            .build();
    }
}
