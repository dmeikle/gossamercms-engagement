package com.gossamercms.chat.dtos;

import com.gossamercms.chat.models.ChatConversationAuditLog;
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
public class ChatConversationAuditLogDto implements DtoWithId{
    private UUID id;
    private UUID chatConversationId;
    private String action;
    private UUID performedBy;
    private UUID createdBy;
    private Instant createdAt;
    private UUID updatedBy;
    private Instant updatedAt;

    public ChatConversationAuditLog toEntity() {
        return ChatConversationAuditLog.builder()
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
}
