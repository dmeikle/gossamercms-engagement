package com.gossamercms.contact.handlers;

import com.gossamercms.contact.data.ContactFormFieldsDbService;
import com.gossamercms.contact.dtos.ContactFormFieldDto;
import com.gossamercms.contact.models.ContactFormField;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class ContactFormFieldsHandler extends BaseHandler<ContactFormField, ContactFormFieldDto> {

    public ContactFormFieldsHandler(ContactFormFieldsDbService db) {
        super(db);
    }
}
