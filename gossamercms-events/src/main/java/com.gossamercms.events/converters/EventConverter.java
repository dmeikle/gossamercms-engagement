package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventsDbService;
import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventConverter
        extends BaseConverter<EventDto>
         implements Converter<String, EventDto> {

    public EventConverter(EventsDbService dbService) {
        super(dbService, EventDto.class);
    }
}

