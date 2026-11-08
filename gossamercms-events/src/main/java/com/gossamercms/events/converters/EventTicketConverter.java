package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventTicketsDbService;
import com.gossamercms.events.dtos.EventTicketDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventTicketConverter
        extends BaseConverter<EventTicketDto>
         implements Converter<String, EventTicketDto> {

    public EventTicketConverter(EventTicketsDbService dbService) {
        super(dbService, EventTicketDto.class);
    }
}

