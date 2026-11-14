package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventOrderItemDto;
import com.gossamercms.events.dtos.responses.EventOrderResponse;
import com.gossamercms.events.models.EventOrderItem;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventOrderItemsDbService extends BaseDbService<EventOrderItem, EventOrderItemDto> {

    public EventOrderItemsDbService(DataSourceManager ds) {
        super(EventOrderItem.class, EventOrderItemDto.class, ds);
    }

    private NamedParameterJdbcTemplate jdbc() {
        return dsManager.get(meta.datasourceKey()).getNamedParameterJdbcTemplate();
    }

    public List<EventOrderResponse.Item> items(UUID orderId) {
        return jdbc().queryForList("""
                SELECT * FROM event_order_items WHERE "orderId"=:id ORDER BY "ticketTypeId"
                """, Map.of("id", orderId)).stream().map(row -> {
                    var item = EventDbRows.mapRowToDto(row, EventOrderItemDto.class);
                    return new EventOrderResponse.Item(item.getId(), item.getTicketTypeId(), item.getQuantity(),
                            item.getUnitPrice(), item.getLineTotal());
                }).toList();
    }

    public void createLine(UUID id, UUID orderId, UUID ticketTypeId, int quantity, BigDecimal unitPrice) {
        jdbc().update("""
                INSERT INTO event_order_items ("id","orderId","ticketTypeId","quantity","unitPrice","lineTotal")
                VALUES (:id,:order,:ticket,:quantity,:price,:total)
                """, Map.of("id", id, "order", orderId, "ticket", ticketTypeId, "quantity", quantity,
                "price", unitPrice, "total", unitPrice.multiply(BigDecimal.valueOf(quantity))));
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
