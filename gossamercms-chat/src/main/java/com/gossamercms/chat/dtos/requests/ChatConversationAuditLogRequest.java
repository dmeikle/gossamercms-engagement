package com.gossamercms.chat.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatConversationAuditLogRequest {
    private UUID chatConversationId;
    private String action;
    private UUID performedBy;

}
