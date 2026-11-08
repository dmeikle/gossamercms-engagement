package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventPaymentsDbService;
import com.gossamercms.events.dtos.EventPaymentDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventPaymentConverter
        extends BaseConverter<EventPaymentDto>
         implements Converter<String, EventPaymentDto> {

    public EventPaymentConverter(EventPaymentsDbService dbService) {
        super(dbService, EventPaymentDto.class);
    }
}

