package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventPaymentDto;
import com.gossamercms.events.models.EventPayment;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventPaymentsDbService extends BaseDbService<EventPayment, EventPaymentDto> {

    public EventPaymentsDbService(DataSourceManager ds) {
        super(EventPayment.class, EventPaymentDto.class, ds);
    }

    @Override
    protected EventPayment mapToEntity(EventPaymentDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventPaymentDto mapToDto(EventPayment entity) {
        return entity.toDto();
    }

    @Override
    protected EventPaymentDto removeExcludedFields(EventPaymentDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
