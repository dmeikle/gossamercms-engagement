package com.gossamercms.events.data;

import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.events.models.Event;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class EventsDbService extends BaseDbService<Event, EventDto> {

    public EventsDbService(DataSourceManager ds) {
        super(Event.class, EventDto.class, ds);
    }

    private NamedParameterJdbcTemplate jdbc() {
        return dsManager.get(meta.datasourceKey()).getNamedParameterJdbcTemplate();
    }

    public boolean canManage(UUID eventId, UUID userId) {
        return Boolean.TRUE.equals(jdbc().queryForObject("""
                SELECT EXISTS (
                  SELECT 1 FROM events e WHERE e."id"=:event AND e."deletedAt" IS NULL AND (
                    e."createdBy"=:user OR EXISTS (
                      SELECT 1 FROM event_organizer_assignments a
                      JOIN event_organizers g ON g."id"=a."organizerId" AND g."deletedAt" IS NULL
                      JOIN event_organizer_members m ON m."organizerId"=a."organizerId"
                      WHERE a."eventId"=e."id" AND m."userId"=:user AND m."deletedAt" IS NULL
                        AND LOWER(m."status")='active' AND LOWER(m."role") IN ('owner','admin','manager','event_manager')
                    )
                  )
                )
                """, Map.of("event", eventId, "user", userId), Boolean.class));
    }

    public void requireEventOnSale(UUID eventId) {
        if (!Boolean.TRUE.equals(jdbc().queryForObject("""
                SELECT EXISTS (SELECT 1 FROM events WHERE "id"=:id AND "deletedAt" IS NULL
                  AND "isPublished"=TRUE AND LOWER("visibility")='public'
                  AND LOWER(COALESCE("status",'')) NOT IN ('cancelled','canceled','completed')
                  AND ("endAt" IS NULL OR "endAt">CURRENT_TIMESTAMP)
                  AND ("registrationStartAt" IS NULL OR "registrationStartAt"<=CURRENT_TIMESTAMP)
                  AND ("registrationEndAt" IS NULL OR "registrationEndAt">CURRENT_TIMESTAMP))
                """, Map.of("id", eventId), Boolean.class))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Event is not open for orders");
        }
    }

    @Override
    protected Event mapToEntity(EventDto dto) {
        return dto.toEntity();
    }

    @Override
    protected EventDto mapToDto(Event entity) {
        return entity.toDto();
    }

    @Override
    protected EventDto removeExcludedFields(EventDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
