package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventOrderDto;
import com.gossamercms.events.models.EventOrder;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventOrdersDbService extends BaseDbService<EventOrder, EventOrderDto> {

    public EventOrdersDbService(DataSourceManager ds) {
        super(EventOrder.class, EventOrderDto.class, ds);
    }

    @Override
    protected EventOrder mapToEntity(EventOrderDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventOrderDto mapToDto(EventOrder entity) {
        return entity.toDto();
    }

    @Override
    protected EventOrderDto removeExcludedFields(EventOrderDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
