package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventOrderItemsDbService;
import com.gossamercms.events.dtos.EventOrderItemDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventOrderItemConverter
        extends BaseConverter<EventOrderItemDto>
         implements Converter<String, EventOrderItemDto> {

    public EventOrderItemConverter(EventOrderItemsDbService dbService) {
        super(dbService, EventOrderItemDto.class);
    }
}

