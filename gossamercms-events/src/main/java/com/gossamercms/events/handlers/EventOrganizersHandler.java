package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventOrganizersDbService;
import com.gossamercms.events.dtos.EventOrganizerDto;
import com.gossamercms.events.dtos.responses.EventListDto;
import com.gossamercms.events.models.EventOrganizer;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.mvc.handlers.BaseHandler;

import java.util.Map;
import java.util.UUID;

@ModuleHandler
public class EventOrganizersHandler extends BaseHandler<EventOrganizer, EventOrganizerDto> {
    private final EventOrganizersDbService organizers;

    public EventOrganizersHandler(EventOrganizersDbService db) {
        super(db);
        this.organizers = db;
    }

    public ListResultset<EventListDto> getEvents(UUID organizerId, int page, int size) {
        return organizers.getEvents(QueryOptions.of(page, size, Map.of("organizerId", organizerId), Map.of()));
    }
}
