package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventCategoryAssignmentsDbService;
import com.gossamercms.events.dtos.EventCategoryAssignmentDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventCategoryAssignmentConverter
        extends BaseConverter<EventCategoryAssignmentDto>
         implements Converter<String, EventCategoryAssignmentDto> {

    public EventCategoryAssignmentConverter(EventCategoryAssignmentsDbService dbService) {
        super(dbService, EventCategoryAssignmentDto.class);
    }
}

