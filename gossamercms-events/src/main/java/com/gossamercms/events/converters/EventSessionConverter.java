package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventSessionsDbService;
import com.gossamercms.events.dtos.EventSessionDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventSessionConverter
        extends BaseConverter<EventSessionDto>
         implements Converter<String, EventSessionDto> {

    public EventSessionConverter(EventSessionsDbService dbService) {
        super(dbService, EventSessionDto.class);
    }
}

