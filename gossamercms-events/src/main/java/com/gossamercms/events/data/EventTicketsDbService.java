package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventTicketDto;
import com.gossamercms.events.dtos.responses.EventTicketListResponse;
import com.gossamercms.events.models.EventTicket;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.*;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventTicketsDbService extends BaseDbService<EventTicket, EventTicketDto> {

    public EventTicketsDbService(DataSourceManager ds) {
        super(EventTicket.class, EventTicketDto.class, ds);
    }

    @Override
    protected EventTicket mapToEntity(EventTicketDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventTicketDto mapToDto(EventTicket entity) {
        return entity.toDto();
    }

    @Override
    protected EventTicketDto removeExcludedFields(EventTicketDto dto) {
        return dto;
    }

    public long getNextPurchaseSequenceValue() {
        DataSourceAdapter ds = dsManager.get(meta.datasourceKey());
        String sql = """
        SELECT nextval('ecommerce_ticket_number_seq')
        """;

        return ds.executeSql(sql, Long.class);
    }

    public ListResultset<EventTicketListResponse> listByUserContext(QueryOptions options) {
        if (!(options.filters().get("userContextId") instanceof UUID)) {
            throw new IllegalArgumentException("A user context ID is required to list tickets");
        }

        String sql = """
                select er.id as registrationId, "registeredAt", er."cancelledAt",
                       e.id as "eventId", e.title, e.slug, e.summary, e."eventType","startAt", "endAt", timezone,"thumbnailImageUrl","isFeatured",
                       et.id as ticketId, et."ticketTypeId", "ticketNumber", "qrTokenHash", et.status, "issuedAt",
                       eo.total
                from event_order_items eoi
                    inner join event_orders eo on eo.id = eoi."orderId"
                inner join event_registrations er on er."orderId" = eo."id"
                inner join event_tickets et on et."registrationId" = er."id"
                inner join events e on e.id = er."eventId"
                 where er."userContextId" = :userContextId and er."status" = 'REGISTERED'
                """;

        String countSql = """
                select count(*) from event_order_items eoi
                    inner join event_orders eo on eo.id = eoi."orderId"
                inner join event_registrations er on er."orderId" = eo."id"
                inner join event_tickets et on et."registrationId" = er."id"
                inner join events e on e.id = er."eventId"
                where er."userContextId" = :userContextId and er."status" = 'REGISTERED'
                """;

        DataSourceAdapter ds = dsManager.get(meta.datasourceKey());

        return ds.findAllBySql(
                sql,
                countSql,
                options.filters(),
                Map.of("userContextId", "er.userContextId", "status", "et.status"),
                Collections.emptyMap(),
                options.page(),
                options.size(),
                (rs, rowNum) -> EventTicketListResponse.builder()
                        .registrationId(rs.getObject("registrationId", UUID.class))
                        .registeredAt(toInstant(rs.getTimestamp("registeredAt")))
                        .cancelledAt(toInstant(rs.getTimestamp("cancelledAt")))
                        .eventId(rs.getObject("eventId", UUID.class))
                        .title(rs.getString("title"))
                        .slug(rs.getString("slug"))
                        .summary(rs.getString("summary"))
                        .eventType(rs.getString("eventType"))
                        .startAt(toInstant(rs.getTimestamp("startAt")))
                        .endAt(toInstant(rs.getTimestamp("endAt")))
                        .timezone(rs.getString("timezone"))
                        .thumbnailImageUrl(rs.getString("thumbnailImageUrl"))
                        .isFeatured(rs.getObject("isFeatured", Boolean.class))
                        .ticketId(rs.getObject("ticketId", UUID.class))
                        .ticketTypeId(rs.getObject("ticketTypeId", UUID.class))
                        .ticketNumber(rs.getString("ticketNumber"))
                        .qrTokenHash(rs.getString("qrTokenHash"))
                        .status(rs.getString("status"))
                        .issuedAt(toInstant(rs.getTimestamp("issuedAt")))
                        .total(rs.getBigDecimal("total"))
                        .build()
        );
    }


}
