package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventWaitlistsDbService;
import com.gossamercms.events.dtos.EventWaitlistDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventWaitlistConverter
        extends BaseConverter<EventWaitlistDto>
         implements Converter<String, EventWaitlistDto> {

    public EventWaitlistConverter(EventWaitlistsDbService dbService) {
        super(dbService, EventWaitlistDto.class);
    }
}

