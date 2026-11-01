package com.gossamercms.subscribers.data;

import com.gossamercms.subscribers.dtos.NewsletterSubscriberDto;
import com.gossamercms.subscribers.models.NewsletterSubscriber;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;


@ModuleDbService
public class NewsletterSubscribersDbService extends BaseDbService<NewsletterSubscriber, NewsletterSubscriberDto> {

    public NewsletterSubscribersDbService(DataSourceManager ds) {
        super(NewsletterSubscriber.class, NewsletterSubscriberDto.class, ds);
    }

    @Override
    protected NewsletterSubscriber mapToEntity(NewsletterSubscriberDto dto) {
        return dto.toEntity();
    }

    @Override
    protected NewsletterSubscriberDto mapToDto(NewsletterSubscriber entity) {
        return entity.toDto();
    }

    @Override
    protected NewsletterSubscriberDto removeExcludedFields(NewsletterSubscriberDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
