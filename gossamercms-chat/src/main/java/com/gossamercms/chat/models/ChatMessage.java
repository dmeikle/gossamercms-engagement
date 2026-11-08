package com.gossamercms.chat.models;

import com.gossamercms.chat.dtos.ChatMessageDto;
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
public class ChatMessage implements BaseModel{
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
    private UUID deletedBy;
    private Instant deletedAt;

    public ChatMessageDto toDto() {
        return ChatMessageDto.builder()
                .id(id)
                .chatConversationId(chatConversationId)
                .senderContextId(senderContextId)
                .recipientContextId(recipientContextId)
                .status(status)
                .messageContent(messageContent)
                .viewedAt(viewedAt)
                .createdBy(createdBy)
                .createdAt(createdAt)
                .updatedBy(updatedBy)
                .updatedAt(updatedAt)
                .build();
    }


    public static final ModelMeta META = ModelMeta.builderWithId("chat_messages")
            .column("id", UUID.class)
            .column("chatConversationId", UUID.class)
            .column("senderContextId", UUID.class)
            .column("recipientContextId", UUID.class)
            .column("status", String.class, 20)
            .column("messageContent", String.class, 1000)
            .column("viewedAt", Instant.class)
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
