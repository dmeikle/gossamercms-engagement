package com.gossamercms.chat.data;

import com.gossamercms.chat.dtos.ChatMessageDto;
import com.gossamercms.chat.models.ChatMessage;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class ChatMessagesDbService extends BaseDbService<ChatMessage, ChatMessageDto> {

    public ChatMessagesDbService(DataSourceManager ds) {
        super(ChatMessage.class, ChatMessageDto.class, ds);
    }

    @Override
    protected ChatMessage mapToEntity(ChatMessageDto dto) {
        return dto.toEntity();
    }

    @Override
    protected ChatMessageDto mapToDto(ChatMessage entity) {
        return entity.toDto();
    }

    @Override
    protected ChatMessageDto removeExcludedFields(ChatMessageDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
