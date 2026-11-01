package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventRefundsDbService;
import com.gossamercms.events.dtos.EventRefundDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventRefundConverter
        extends BaseConverter<EventRefundDto>
         implements Converter<String, EventRefundDto> {

    public EventRefundConverter(EventRefundsDbService dbService) {
        super(dbService, EventRefundDto.class);
    }
}

