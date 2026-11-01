package com.gossamercms.subscribers.converters;

import com.gossamercms.subscribers.data.NewslettersDbService;
import com.gossamercms.subscribers.dtos.NewsletterDto;
import com.gossamercms.mvc.annotations.ModuleConverter;
import org.springframework.core.convert.converter.Converter;
import com.gossamercms.mvc.converters.BaseConverter;


@ModuleConverter
public class NewsletterConverter
        extends BaseConverter<NewsletterDto>
         implements Converter<String, NewsletterDto> {

    public NewsletterConverter(NewslettersDbService dbService) {
        super(dbService, NewsletterDto.class);
    }
}

