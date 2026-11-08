package com.gossamercms.chat.handlers;

import com.gossamercms.chat.data.ChatConversationAuditLogsDbService;
import com.gossamercms.chat.dtos.ChatConversationAuditLogDto;
import com.gossamercms.chat.models.ChatConversationAuditLog;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class ChatConversationAuditLogsHandler extends BaseHandler<ChatConversationAuditLog, ChatConversationAuditLogDto> {

    public ChatConversationAuditLogsHandler(ChatConversationAuditLogsDbService db) {
        super(db);
    }
}
