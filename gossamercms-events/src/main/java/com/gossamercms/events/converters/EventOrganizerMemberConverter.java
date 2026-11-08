package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventOrganizerMembersDbService;
import com.gossamercms.events.dtos.EventOrganizerMemberDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventOrganizerMemberConverter
        extends BaseConverter<EventOrganizerMemberDto>
         implements Converter<String, EventOrganizerMemberDto> {

    public EventOrganizerMemberConverter(EventOrganizerMembersDbService dbService) {
        super(dbService, EventOrganizerMemberDto.class);
    }
}

