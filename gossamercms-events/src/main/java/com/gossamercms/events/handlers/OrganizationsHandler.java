package com.gossamercms.events.handlers;

import com.gossamercms.events.data.OrganizationsDbService;
import com.gossamercms.events.dtos.OrganizationDto;
import com.gossamercms.events.models.Organization;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class OrganizationsHandler extends BaseHandler<Organization, OrganizationDto> {

    public OrganizationsHandler(OrganizationsDbService db) {
        super(db);
    }
}
