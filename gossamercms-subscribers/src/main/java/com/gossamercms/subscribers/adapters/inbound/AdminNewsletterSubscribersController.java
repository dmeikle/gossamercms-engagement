package com.gossamercms.subscribers.adapters.inbound;


import com.gossamercms.subscribers.dtos.NewsletterSubscriberDto;
import com.gossamercms.subscribers.handlers.NewsletterSubscribersHandler;
import com.gossamercms.subscribers.models.NewsletterSubscriber;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/newsletter-subscribers")
public class AdminNewsletterSubscribersController extends BaseController<NewsletterSubscriber, NewsletterSubscriberDto> {

    public AdminNewsletterSubscribersController(NewsletterSubscribersHandler handler) {
        super(handler);
    }
}
