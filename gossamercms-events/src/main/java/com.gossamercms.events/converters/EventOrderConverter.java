package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventOrdersDbService;
import com.gossamercms.events.dtos.EventOrderDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventOrderConverter
        extends BaseConverter<EventOrderDto>
         implements Converter<String, EventOrderDto> {

    public EventOrderConverter(EventOrdersDbService dbService) {
        super(dbService, EventOrderDto.class);
    }
}

