package com.gossamercms.subscribers.adapters.inbound;


import com.gossamercms.subscribers.dtos.NewsletterDto;
import com.gossamercms.subscribers.handlers.NewslettersHandler;
import com.gossamercms.subscribers.models.Newsletter;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/newsletters")
public class AdminNewslettersController extends BaseController<Newsletter, NewsletterDto> {

    public AdminNewslettersController(NewslettersHandler handler) {
        super(handler);
    }
}
