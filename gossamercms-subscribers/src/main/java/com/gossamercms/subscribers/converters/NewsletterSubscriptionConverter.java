package com.gossamercms.subscribers.converters;

import com.gossamercms.subscribers.data.NewsletterSubscriptionsDbService;
import com.gossamercms.subscribers.dtos.NewsletterSubscriptionDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;


@ModuleConverter
public class NewsletterSubscriptionConverter
        extends BaseConverter<NewsletterSubscriptionDto>
         implements Converter<String, NewsletterSubscriptionDto> {

    public NewsletterSubscriptionConverter(NewsletterSubscriptionsDbService dbService) {
        super(dbService, NewsletterSubscriptionDto.class);
    }
}

