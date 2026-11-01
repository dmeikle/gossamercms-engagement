package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventTicketTypesDbService;
import com.gossamercms.events.dtos.EventTicketTypeDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventTicketTypeConverter
        extends BaseConverter<EventTicketTypeDto>
         implements Converter<String, EventTicketTypeDto> {

    public EventTicketTypeConverter(EventTicketTypesDbService dbService) {
        super(dbService, EventTicketTypeDto.class);
    }
}

