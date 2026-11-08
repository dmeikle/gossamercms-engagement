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
public class ChatConversationAuditLogListResponse {
    private UUID chatConversationId;
    private String action;
    private UUID performedBy;
    private Instant createdAt;

}