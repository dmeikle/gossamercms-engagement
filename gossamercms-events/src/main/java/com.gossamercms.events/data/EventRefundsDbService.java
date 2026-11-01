package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventRefundDto;
import com.gossamercms.events.models.EventRefund;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventRefundsDbService extends BaseDbService<EventRefund, EventRefundDto> {

    public EventRefundsDbService(DataSourceManager ds) {
        super(EventRefund.class, EventRefundDto.class, ds);
    }

    @Override
    protected EventRefund mapToEntity(EventRefundDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventRefundDto mapToDto(EventRefund entity) {
        return entity.toDto();
    }

    @Override
    protected EventRefundDto removeExcludedFields(EventRefundDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
