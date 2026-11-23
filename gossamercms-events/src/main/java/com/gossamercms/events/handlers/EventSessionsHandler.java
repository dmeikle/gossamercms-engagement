package com.gossamercms.events.handlers;

import com.gossamercms.events.data.EventSessionsDbService;
import com.gossamercms.events.dtos.EventSessionDto;
import com.gossamercms.events.dtos.responses.AdminEventSessionListResponse;
import com.gossamercms.events.dtos.responses.EventSessionOptionListResponse;
import com.gossamercms.events.models.EventSession;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class EventSessionsHandler extends BaseHandler<EventSession, EventSessionDto> {

    public EventSessionsHandler(EventSessionsDbService db) {
        super(db);
    }

    public ListResultset<EventSessionOptionListResponse> getOptionList(QueryOptions options) {
        return ((EventSessionsDbService) db).getOptionList(options);
    }
}
