package com.gossamercms.subscribers.handlers;

import com.gossamercms.subscribers.data.NewsletterSubscriptionsDbService;
import com.gossamercms.subscribers.dtos.NewsletterSubscriptionDto;
import com.gossamercms.subscribers.models.NewsletterSubscription;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class NewsletterSubscriptionsHandler extends BaseHandler<NewsletterSubscription, NewsletterSubscriptionDto> {

    public NewsletterSubscriptionsHandler(NewsletterSubscriptionsDbService db) {
        super(db);
    }
}
