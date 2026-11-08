package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventPromoCodeDto;
import com.gossamercms.events.models.EventPromoCode;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventPromoCodesDbService extends BaseDbService<EventPromoCode, EventPromoCodeDto> {

    public EventPromoCodesDbService(DataSourceManager ds) {
        super(EventPromoCode.class, EventPromoCodeDto.class, ds);
    }

    @Override
    protected EventPromoCode mapToEntity(EventPromoCodeDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventPromoCodeDto mapToDto(EventPromoCode entity) {
        return entity.toDto();
    }

    @Override
    protected EventPromoCodeDto removeExcludedFields(EventPromoCodeDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
