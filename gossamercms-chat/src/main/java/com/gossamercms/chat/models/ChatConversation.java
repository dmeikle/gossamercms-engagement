package com.gossamercms.chat.models;

import com.gossamercms.chat.dtos.ChatConversationDto;
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
public class ChatConversation implements BaseModel{
    private UUID id;
    private UUID senderContextId;
    private UUID assignedStaffContextId;
    private String topic;
    private String status;
    private UUID createdBy;
    private Instant createdAt;
    private UUID updatedBy;
    private Instant updatedAt;
    private UUID deletedBy;
    private Instant deletedAt;

    public ChatConversationDto toDto() {
        return ChatConversationDto.builder()
            .id(id)
            .senderContextId(senderContextId)
            .assignedStaffContextId(assignedStaffContextId)
            .topic(topic)
            .status(status)
            .createdBy(createdBy)
            .createdAt(createdAt)
            .updatedBy(updatedBy)
            .updatedAt(updatedAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("chat_conversations")
    .column("id", UUID.class)
    .column("senderContextId", UUID.class)
    .column("assignedStaffContextId", UUID.class)
    .column("topic", String.class, 20)
    .column("status", String.class, 20)
    .column("createdBy", UUID.class)
    .column("createdAt", Instant.class)
    .column("updatedBy", UUID.class)
    .column("updatedAt", Instant.class)
    .column("deletedBy", UUID.class)
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
