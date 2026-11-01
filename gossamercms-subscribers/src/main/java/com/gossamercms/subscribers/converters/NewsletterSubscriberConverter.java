package com.gossamercms.subscribers.converters;

import com.gossamercms.subscribers.data.NewsletterSubscribersDbService;
import com.gossamercms.subscribers.dtos.NewsletterSubscriberDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;


@ModuleConverter
public class NewsletterSubscriberConverter
        extends BaseConverter<NewsletterSubscriberDto>
         implements Converter<String, NewsletterSubscriberDto> {

    public NewsletterSubscriberConverter(NewsletterSubscribersDbService dbService) {
        super(dbService, NewsletterSubscriberDto.class);
    }
}

