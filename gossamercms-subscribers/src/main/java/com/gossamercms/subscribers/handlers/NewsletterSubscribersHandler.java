package com.gossamercms.subscribers.handlers;

import com.gossamercms.subscribers.data.NewsletterSubscribersDbService;
import com.gossamercms.subscribers.dtos.NewsletterSubscriberDto;
import com.gossamercms.subscribers.models.NewsletterSubscriber;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class NewsletterSubscribersHandler extends BaseHandler<NewsletterSubscriber, NewsletterSubscriberDto> {

    public NewsletterSubscribersHandler(NewsletterSubscribersDbService db) {
        super(db);
    }
}
