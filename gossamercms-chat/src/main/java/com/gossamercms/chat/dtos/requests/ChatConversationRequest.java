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
public class ChatConversationRequest {
    private UUID chatConversationId;
    private UUID senderContextId;
    private UUID assignedStaffContextId;
    private String messageContent;
    private String topic;
    private String status;

}
