package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventLocationsDbService;
import com.gossamercms.events.dtos.EventLocationDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventLocationConverter
        extends BaseConverter<EventLocationDto>
         implements Converter<String, EventLocationDto> {

    public EventLocationConverter(EventLocationsDbService dbService) {
        super(dbService, EventLocationDto.class);
    }
}

