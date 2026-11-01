package com.gossamercms.subscribers.adapters.inbound;


import com.gossamercms.subscribers.dtos.NewsletterSubscriptionDto;
import com.gossamercms.subscribers.handlers.NewsletterSubscriptionsHandler;
import com.gossamercms.subscribers.models.NewsletterSubscription;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/newsletter-subscriptions")
public class AdminNewsletterSubscriptionsController extends BaseController<NewsletterSubscription, NewsletterSubscriptionDto> {

    public AdminNewsletterSubscriptionsController(NewsletterSubscriptionsHandler handler) {
        super(handler);
    }
}
