package com.gossamercms.chat.services;

import com.gossamercms.chat.data.ChatConversationAuditLogsDbService;
import com.gossamercms.chat.data.ChatConversationsDbService;
import com.gossamercms.chat.data.ChatMessagesDbService;
import com.gossamercms.chat.dtos.ChatConversationAuditLogDto;
import com.gossamercms.chat.dtos.ChatConversationDto;
import com.gossamercms.chat.dtos.ChatMessageDto;
import com.gossamercms.chat.dtos.requests.ChatConversationRequest;
import com.gossamercms.chat.dtos.requests.ChatMessageRequest;
import com.gossamercms.chat.dtos.responses.ChatConversationResponse;
import com.gossamercms.chat.dtos.responses.ChatMessageResponse;
import com.gossamercms.chat.enums.ChatConversationAuditLogStatus;
import com.gossamercms.chat.enums.ChatConversationStatus;
import com.gossamercms.chat.enums.ChatMessageStatus;
import com.gossamercms.mvc.annotations.ModuleService;
import com.gossamercms.security.jwt.JwtUser;
import lombok.RequiredArgsConstructor;

@ModuleService
@RequiredArgsConstructor
public class ChatService {

    private final ChatConversationsDbService chatConversationsDbService;
    private final ChatMessagesDbService chatMessagesDbService;
    private final ChatConversationAuditLogsDbService chatConversationAuditLogsDbService;

    public ChatConversationResponse startConversation(JwtUser jwtUser, ChatConversationRequest request) {
        // 1. Create a new chat conversation
        ChatConversationDto conversation = chatConversationsDbService.create(jwtUser.getUserContextId(),
               ChatConversationDto.builder()
                        .topic(request.getTopic())
                        .senderContextId(jwtUser.getUserContextId())
                        .status(ChatConversationStatus.WAITING.toString())
                        .build());

        // 2. Add the initial message to the conversation
        ChatMessageDto message = chatMessagesDbService.create(jwtUser.getUserContextId(),
                ChatMessageDto.builder()
                        .chatConversationId(conversation.getId())
                        .senderContextId(jwtUser.getUserContextId())
                        .status(ChatMessageStatus.PENDING.toString())
                        .messageContent(request.getMessageContent())
                        .build());

        // 3. Log the action in the audit logs
        chatConversationAuditLogsDbService.create(jwtUser.getUserContextId(),
                ChatConversationAuditLogDto.builder()
                        .chatConversationId(conversation.getId())
                        .action(ChatConversationAuditLogStatus.CREATED.toString())
                        .performedBy(jwtUser.getUserContextId())
                        .build());

        // 4. Return the conversation details
        return ChatConversationResponse.builder()
                .chatConversationId(conversation.getId())
                .topic(conversation.getTopic())
                .message(ChatMessageResponse.builder()
                        .chatConversationId(message.getChatConversationId())
                        .messageContent(message.getMessageContent())
                        .status(message.getStatus())
                        .build())
                .senderContextId(conversation.getSenderContextId())
                .status(conversation.getStatus())
                .createdAt(conversation.getCreatedAt())
                .build();
    }

    public ChatMessageResponse sendMessage(JwtUser jwtUser, ChatConversationDto conversation, ChatMessageRequest request) {
        // 1. Create a new chat message
        ChatMessageDto message = chatMessagesDbService.create(jwtUser.getUserContextId(),
                ChatMessageDto.builder()
                        .chatConversationId(conversation.getId())
                        .senderContextId(jwtUser.getUserContextId())
                        .status(ChatMessageStatus.PENDING.toString())
                        .messageContent(request.getMessageContent())
                        .build());

        // 2. Log the action in the audit logs
        chatConversationAuditLogsDbService.create(jwtUser.getUserContextId(),
                ChatConversationAuditLogDto.builder()
                        .chatConversationId(conversation.getId())
                        .action(ChatConversationAuditLogStatus.CREATED.toString())
                        .performedBy(jwtUser.getUserContextId())
                        .build());

        // 3. Return the message details
        return ChatMessageResponse.builder()
                .chatConversationId(message.getChatConversationId())
                .messageContent(message.getMessageContent())
                .status(message.getStatus())
                .viewedAt(message.getViewedAt())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
