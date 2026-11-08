package com.gossamercms.chat.converters;

import com.gossamercms.chat.data.ChatMessagesDbService;
import com.gossamercms.chat.dtos.ChatMessageDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class ChatMessageConverter
        extends BaseConverter<ChatMessageDto>
         implements Converter<String, ChatMessageDto> {

    public ChatMessageConverter(ChatMessagesDbService dbService) {
        super(dbService, ChatMessageDto.class);
    }
}

