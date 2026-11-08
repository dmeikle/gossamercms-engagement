package com.gossamercms.contact.data;

import com.gossamercms.contact.dtos.ContactSubmissionAttachmentDto;
import com.gossamercms.contact.models.ContactSubmissionAttachment;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import com.gossamercms.mvc.data.ListResultset;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@ModuleDbService
public class ContactSubmissionAttachmentsDbService extends BaseDbService<ContactSubmissionAttachment, ContactSubmissionAttachmentDto> {

    public ContactSubmissionAttachmentsDbService(DataSourceManager ds) {
        super(ContactSubmissionAttachment.class, ContactSubmissionAttachmentDto.class, ds);
    }

    @Override
    protected ContactSubmissionAttachment mapToEntity(ContactSubmissionAttachmentDto dto) {
        return dto.toEntity();
    }

    @Override
    protected ContactSubmissionAttachmentDto mapToDto(ContactSubmissionAttachment entity) {
        return entity.toDto();
    }

    @Override
    protected ContactSubmissionAttachmentDto removeExcludedFields(ContactSubmissionAttachmentDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
