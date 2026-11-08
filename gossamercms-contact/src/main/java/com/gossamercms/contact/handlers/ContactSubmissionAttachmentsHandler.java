package com.gossamercms.contact.handlers;

import com.gossamercms.contact.data.ContactSubmissionAttachmentsDbService;
import com.gossamercms.contact.dtos.ContactSubmissionAttachmentDto;
import com.gossamercms.contact.models.ContactSubmissionAttachment;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class ContactSubmissionAttachmentsHandler extends BaseHandler<ContactSubmissionAttachment, ContactSubmissionAttachmentDto> {

    public ContactSubmissionAttachmentsHandler(ContactSubmissionAttachmentsDbService db) {
        super(db);
    }
}
