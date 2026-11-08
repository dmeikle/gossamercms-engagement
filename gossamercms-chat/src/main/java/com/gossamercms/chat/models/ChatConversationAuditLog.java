package com.gossamercms.chat.models;

import com.gossamercms.chat.dtos.ChatConversationAuditLogDto;
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
public class ChatConversationAuditLog implements BaseModel{
    private UUID id;
    private UUID chatConversationId;
    private String action;
    private UUID performedBy;
    private UUID createdBy;
    private Instant createdAt;
    private UUID updatedBy;
    private Instant updatedAt;
    private UUID deletedBy;
    private Instant deletedAt;

    public ChatConversationAuditLogDto toDto() {
        return ChatConversationAuditLogDto.builder()
            .id(id)
            .chatConversationId(chatConversationId)
            .action(action)
            .performedBy(performedBy)
            .createdBy(createdBy)
            .createdAt(createdAt)
            .updatedBy(updatedBy)
            .updatedAt(updatedAt)
            .build();
    }


public static final ModelMeta META = ModelMeta.builderWithId("chat_conversation_audit_logs")
    .column("id", UUID.class)
    .column("chatConversationId", UUID.class)
    .column("action", String.class, 20)
    .column("performedBy", UUID.class)
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
