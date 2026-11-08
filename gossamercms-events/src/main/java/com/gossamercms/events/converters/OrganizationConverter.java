package com.gossamercms.events.converters;

import com.gossamercms.events.data.OrganizationsDbService;
import com.gossamercms.events.dtos.OrganizationDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;

import java.util.UUID;


@ModuleConverter
public class OrganizationConverter
        extends BaseConverter<OrganizationDto>
         implements Converter<String, OrganizationDto> {

    public OrganizationConverter(OrganizationsDbService dbService) {
        super(dbService, OrganizationDto.class);
    }
}

