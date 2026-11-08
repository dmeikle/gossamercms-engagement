package com.gossamercms.events.converters;

import com.gossamercms.events.data.EventPromoCodesDbService;
import com.gossamercms.events.dtos.EventPromoCodeDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class EventPromoCodeConverter
        extends BaseConverter<EventPromoCodeDto>
         implements Converter<String, EventPromoCodeDto> {

    public EventPromoCodeConverter(EventPromoCodesDbService dbService) {
        super(dbService, EventPromoCodeDto.class);
    }
}

