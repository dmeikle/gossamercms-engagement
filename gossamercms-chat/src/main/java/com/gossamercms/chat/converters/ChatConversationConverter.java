package com.gossamercms.chat.converters;

import com.gossamercms.chat.data.ChatConversationsDbService;
import com.gossamercms.chat.dtos.ChatConversationDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class ChatConversationConverter
        extends BaseConverter<ChatConversationDto>
         implements Converter<String, ChatConversationDto> {

    public ChatConversationConverter(ChatConversationsDbService dbService) {
        super(dbService, ChatConversationDto.class);
    }
}

