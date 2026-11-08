package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventCategorysDbService;
import com.gossamercms.events.dtos.EventCategoryDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventCategoryConverter
        extends BaseConverter<EventCategoryDto>
         implements Converter<String, EventCategoryDto> {

    public EventCategoryConverter(EventCategorysDbService dbService) {
        super(dbService, EventCategoryDto.class);
    }
}

