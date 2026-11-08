package com.gossamercms.chat.dtos;

import com.gossamercms.chat.models.ChatConversation;
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
public class ChatConversationDto implements DtoWithId{
    private UUID id;
    private UUID senderContextId;
    private UUID assignedStaffContextId;
    private String topic;
    private String status;
    private UUID createdBy;
    private Instant createdAt;
    private UUID updatedBy;
    private Instant updatedAt;

    public ChatConversation toEntity() {
        return ChatConversation.builder()
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
}
