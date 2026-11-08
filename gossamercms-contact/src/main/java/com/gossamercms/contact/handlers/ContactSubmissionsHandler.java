package com.gossamercms.contact.handlers;

import com.gossamercms.contact.data.ContactSubmissionsDbService;
import com.gossamercms.contact.dtos.ContactSubmissionDto;
import com.gossamercms.contact.models.ContactSubmission;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class ContactSubmissionsHandler extends BaseHandler<ContactSubmission, ContactSubmissionDto> {

    public ContactSubmissionsHandler(ContactSubmissionsDbService db) {
        super(db);
    }
}
