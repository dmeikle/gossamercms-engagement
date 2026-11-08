package com.gossamercms.chat.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageListResponse {
    private UUID chatConversationId;
    private UUID senderContextId;
    private UUID recipientContextId;
    private String status;
    private Instant viewedAt;
    private Instant createdAt;

}