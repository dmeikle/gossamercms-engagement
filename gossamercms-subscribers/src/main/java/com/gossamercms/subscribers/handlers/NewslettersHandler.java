package com.gossamercms.subscribers.handlers;

import com.gossamercms.subscribers.data.NewslettersDbService;
import com.gossamercms.subscribers.dtos.NewsletterDto;
import com.gossamercms.subscribers.models.Newsletter;
import com.gossamercms.mvc.annotations.ModuleHandler;
import com.gossamercms.mvc.handlers.BaseHandler;

@ModuleHandler
public class NewslettersHandler extends BaseHandler<Newsletter, NewsletterDto> {

    public NewslettersHandler(NewslettersDbService db) {
        super(db);
    }
}
