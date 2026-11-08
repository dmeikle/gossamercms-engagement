package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventOrganizersDbService;
import com.gossamercms.events.dtos.EventOrganizerDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventOrganizerConverter
        extends BaseConverter<EventOrganizerDto>
         implements Converter<String, EventOrganizerDto> {

    public EventOrganizerConverter(EventOrganizersDbService dbService) {
        super(dbService, EventOrganizerDto.class);
    }
}

