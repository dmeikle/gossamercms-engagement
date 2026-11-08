package com.gossamercms.chat.data;

import com.gossamercms.chat.dtos.ChatConversationDto;
import com.gossamercms.chat.models.ChatConversation;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class ChatConversationsDbService extends BaseDbService<ChatConversation, ChatConversationDto> {

    public ChatConversationsDbService(DataSourceManager ds) {
        super(ChatConversation.class, ChatConversationDto.class, ds);
    }

    @Override
    protected ChatConversation mapToEntity(ChatConversationDto dto) {
        return dto.toEntity();
    }

    @Override
    protected ChatConversationDto mapToDto(ChatConversation entity) {
        return entity.toDto();
    }

    @Override
    protected ChatConversationDto removeExcludedFields(ChatConversationDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
