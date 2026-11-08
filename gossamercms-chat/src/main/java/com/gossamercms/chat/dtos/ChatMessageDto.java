package com.gossamercms.chat.dtos;

import com.gossamercms.chat.models.ChatMessage;
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
public class ChatMessageDto implements DtoWithId{
    private UUID id;
    private UUID chatConversationId;
    private UUID senderContextId;
    private UUID recipientContextId;
    private String messageContent;
    private String status;
    private Instant viewedAt;
    private UUID createdBy;
    private Instant createdAt;
    private UUID updatedBy;
    private Instant updatedAt;

    public ChatMessage toEntity() {
        return ChatMessage.builder()
            .id(id)
            .chatConversationId(chatConversationId)
            .senderContextId(senderContextId)
            .recipientContextId(recipientContextId)
            .messageContent(messageContent)
            .status(status)
            .viewedAt(viewedAt)
            .createdBy(createdBy)
            .createdAt(createdAt)
            .updatedBy(updatedBy)
            .updatedAt(updatedAt)
            .build();
    }
}
