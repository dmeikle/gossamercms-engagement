package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventOrganizerAssignmentsDbService;
import com.gossamercms.events.dtos.EventOrganizerAssignmentDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventOrganizerAssignmentConverter
        extends BaseConverter<EventOrganizerAssignmentDto>
         implements Converter<String, EventOrganizerAssignmentDto> {

    public EventOrganizerAssignmentConverter(EventOrganizerAssignmentsDbService dbService) {
        super(dbService, EventOrganizerAssignmentDto.class);
    }
}

