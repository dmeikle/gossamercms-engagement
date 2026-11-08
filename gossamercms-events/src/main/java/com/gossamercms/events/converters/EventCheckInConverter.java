package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventCheckInsDbService;
import com.gossamercms.events.dtos.EventCheckInDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventCheckInConverter
        extends BaseConverter<EventCheckInDto>
         implements Converter<String, EventCheckInDto> {

    public EventCheckInConverter(EventCheckInsDbService dbService) {
        super(dbService, EventCheckInDto.class);
    }
}

