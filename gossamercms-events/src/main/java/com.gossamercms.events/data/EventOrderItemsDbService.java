package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventOrderItemDto;
import com.gossamercms.events.models.EventOrderItem;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventOrderItemsDbService extends BaseDbService<EventOrderItem, EventOrderItemDto> {

    public EventOrderItemsDbService(DataSourceManager ds) {
        super(EventOrderItem.class, EventOrderItemDto.class, ds);
    }

    @Override
    protected EventOrderItem mapToEntity(EventOrderItemDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventOrderItemDto mapToDto(EventOrderItem entity) {
        return entity.toDto();
    }

    @Override
    protected EventOrderItemDto removeExcludedFields(EventOrderItemDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
