package com.gossamercms.events.data;

import com.gossamercms.events.dtos.TicketTypeDto;
import com.gossamercms.events.models.TicketType;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;

@ModuleDbService
public class TicketTypesDbService extends BaseDbService<TicketType, TicketTypeDto> {
    public TicketTypesDbService(DataSourceManager ds) {
        super(TicketType.class, TicketTypeDto.class, ds);
    }
    @Override
    protected TicketType mapToEntity(TicketTypeDto dto) {
        return dto.toEntity();
    }

    @Override
    protected TicketTypeDto mapToDto(TicketType entity) {
        return entity.toDto();
    }

    @Override
    protected TicketTypeDto removeExcludedFields(TicketTypeDto dto) {
        return dto;
    }
}
