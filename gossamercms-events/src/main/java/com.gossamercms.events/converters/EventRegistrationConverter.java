package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventRegistrationsDbService;
import com.gossamercms.events.dtos.EventRegistrationDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventRegistrationConverter
        extends BaseConverter<EventRegistrationDto>
         implements Converter<String, EventRegistrationDto> {

    public EventRegistrationConverter(EventRegistrationsDbService dbService) {
        super(dbService, EventRegistrationDto.class);
    }
}

