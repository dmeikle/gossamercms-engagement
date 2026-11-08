package com.gossamercms.contact.services;

import com.gossamercms.mvc.annotations.ModuleService;
import com.gossamercms.notifications.services.EmailService;
import org.springframework.mail.javamail.JavaMailSender;


@ModuleService
public class ContactFormEmailService extends EmailService {
    public ContactFormEmailService(JavaMailSender javaMailSender) {
        super(javaMailSender);
    }
}
