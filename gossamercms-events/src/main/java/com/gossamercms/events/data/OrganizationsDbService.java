package com.gossamercms.events.data;

import com.gossamercms.events.dtos.OrganizationDto;
import com.gossamercms.events.models.Organization;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;



@ModuleDbService
public class OrganizationsDbService extends BaseDbService<Organization, OrganizationDto> {

    public OrganizationsDbService(DataSourceManager ds) {
        super(Organization.class, OrganizationDto.class, ds);
    }

    @Override
    protected Organization mapToEntity(OrganizationDto dto) {
        return dto.toEntity();
    }

    @Override
    protected OrganizationDto mapToDto(Organization entity) {
        return entity.toDto();
    }

    @Override
    protected OrganizationDto removeExcludedFields(OrganizationDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found");
    }

}
