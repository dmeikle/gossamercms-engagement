package com.gossamercms.chat.converters;

import com.gossamercms.chat.data.ChatConversationAuditLogsDbService;
import com.gossamercms.chat.dtos.ChatConversationAuditLogDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class ChatConversationAuditLogConverter
        extends BaseConverter<ChatConversationAuditLogDto>
         implements Converter<String, ChatConversationAuditLogDto> {

    public ChatConversationAuditLogConverter(ChatConversationAuditLogsDbService dbService) {
        super(dbService, ChatConversationAuditLogDto.class);
    }
}

