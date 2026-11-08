package com.gossamercms.contact.handlers;

import com.gossamercms.contact.data.ContactFormsDbService;
import com.gossamercms.contact.dtos.ContactFormDto;
import com.gossamercms.contact.models.ContactForm;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class ContactFormsHandler extends BaseHandler<ContactForm, ContactFormDto> {

    public ContactFormsHandler(ContactFormsDbService db) {
        super(db);
    }
}
