package com.gossamercms.events.models;

import com.gossamercms.events.dtos.EventCheckInDto;
import com.gossamercms.mvc.models.BaseModel;
import com.gossamercms.mvc.models.ModelMeta;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCheckIn implements BaseModel{
    private UUID id;
    private UUID ticketId;
    private UUID sessionId;
    private UUID checkedInBy;
    private Instant checkedInAt;
    private String method;
    private Instant createdAt;

    public EventCheckInDto toDto() {
        return EventCheckInDto.builder()
            .id(id)
            .ticketId(ticketId)
            .sessionId(sessionId)
            .checkedInBy(checkedInBy)
            .checkedInAt(checkedInAt)
            .method(method)
            .createdAt(createdAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("event_check_ins")
    .column("id", UUID.class)
    .column("ticketId", UUID.class)
    .column("sessionId", UUID.class)
    .column("checkedInBy", UUID.class)
    .column("checkedInAt", Instant.class)
    .column("method", String.class)
    .column("createdAt", Instant.class)
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
