package com.gossamercms.chat.adapters.inbound;


import com.gossamercms.chat.dtos.ChatConversationAuditLogDto;
import com.gossamercms.chat.dtos.requests.ChatConversationAuditLogRequest;
import com.gossamercms.chat.dtos.responses.ChatConversationAuditLogListResponse;
import com.gossamercms.chat.dtos.responses.ChatConversationAuditLogResponse;
import com.gossamercms.chat.handlers.ChatConversationAuditLogsHandler;
import com.gossamercms.chat.models.ChatConversationAuditLog;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/chat-conversation-audit-logs")
public class ChatConversationAuditLogsController extends BaseController<ChatConversationAuditLog, ChatConversationAuditLogDto, ChatConversationAuditLogRequest,  ChatConversationAuditLogResponse, ChatConversationAuditLogListResponse> {

    public ChatConversationAuditLogsController(ChatConversationAuditLogsHandler handler) {
        super(handler, ChatConversationAuditLogListResponse.class);
    }
    @Override
        protected ChatConversationAuditLogDto toDto(ChatConversationAuditLogRequest request) {
            return ChatConversationAuditLogDto.builder()
                        .chatConversationId(request.getChatConversationId())
                        .action(request.getAction())
                        .performedBy(request.getPerformedBy())
                        .build();

        }

        @Override
        protected ChatConversationAuditLogResponse toResponse(ChatConversationAuditLogDto dto) {
            return ChatConversationAuditLogResponse.builder()
                           .chatConversationId(dto.getChatConversationId())
                           .action(dto.getAction())
                           .performedBy(dto.getPerformedBy())
                           .createdAt(dto.getCreatedAt())
                           .build();

        }
}
