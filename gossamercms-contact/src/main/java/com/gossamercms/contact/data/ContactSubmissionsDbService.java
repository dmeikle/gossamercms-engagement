package com.gossamercms.contact.data;

import com.gossamercms.contact.dtos.ContactSubmissionDto;
import com.gossamercms.contact.dtos.responses.AdminContactSubmissionListResponse;
import com.gossamercms.contact.models.ContactSubmission;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;


@ModuleDbService
public class ContactSubmissionsDbService extends BaseDbService<ContactSubmission, ContactSubmissionDto> {

    public ContactSubmissionsDbService(DataSourceManager ds) {
        super(ContactSubmission.class, ContactSubmissionDto.class, ds);
    }

    @Override
    protected ContactSubmission mapToEntity(ContactSubmissionDto dto) {
        return dto.toEntity();
    }

    @Override
    protected ContactSubmissionDto mapToDto(ContactSubmission entity) {
        return entity.toDto();
    }

    @Override
    protected ContactSubmissionDto removeExcludedFields(ContactSubmissionDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }
    @Override
    public <ResultDto> ListResultset<ResultDto> getAll(QueryOptions options, Class<ResultDto> resultDtoClass) {
        String sql = """
                select s.*, cf.slug, cf.name from contact_submissions s 
                inner join contact_forms cf on s."contactFormId" = cf.id
                /*WHERE_CLAUSE*/             
                """;
        String countSql = """
                select count(*) from contact_submissions s
                inner join contact_forms cf on s."contactFormId" = cf.id  
                /*WHERE_CLAUSE*/     """;
        DataSourceAdapter ds = dsManager.get(meta.datasourceKey());

        return ds.findAllBySql(
                sql,
                countSql,
                options.filters(),  // must contain "providerId" -> UUID
                Collections.emptyMap(),
                Collections.emptyMap(),
                options.page(),
                options.size(),
                (rs, rowNum) -> resultDtoClass.cast(AdminContactSubmissionListResponse.builder()
                        .id(rs.getObject("id", UUID.class))
                        .name(rs.getString("name"))
                        .assignedTo(rs.getObject("assignedTo", UUID.class))
                        .contactFormId(rs.getObject("contactFormId", UUID.class))
                        .slug(rs.getString("slug"))
                        .status(rs.getString("status"))
                        .submittedAt( rs.getTimestamp("submittedAt").toInstant())
                        .resolvedAt(rs.getTimestamp("resolvedAt") != null ? rs.getTimestamp("resolvedAt").toInstant() : null)
                        .priority(rs.getString("priority"))
                        .subject(rs.getString("subject"))
                        .submitterEmail(rs.getString("submitterEmail"))
                        .submitterName(rs.getString("submitterName"))
                        .build())

        );
    }
}
