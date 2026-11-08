package com.gossamercms.chat.data;

import com.gossamercms.chat.dtos.ChatConversationAuditLogDto;
import com.gossamercms.chat.models.ChatConversationAuditLog;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class ChatConversationAuditLogsDbService extends BaseDbService<ChatConversationAuditLog, ChatConversationAuditLogDto> {

    public ChatConversationAuditLogsDbService(DataSourceManager ds) {
        super(ChatConversationAuditLog.class, ChatConversationAuditLogDto.class, ds);
    }

    @Override
    protected ChatConversationAuditLog mapToEntity(ChatConversationAuditLogDto dto) {
        return dto.toEntity();
    }

    @Override
    protected ChatConversationAuditLogDto mapToDto(ChatConversationAuditLog entity) {
        return entity.toDto();
    }

    @Override
    protected ChatConversationAuditLogDto removeExcludedFields(ChatConversationAuditLogDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
