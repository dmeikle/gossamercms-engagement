package com.gossamercms.subscribers.data;

import com.gossamercms.subscribers.dtos.NewsletterDto;
import com.gossamercms.subscribers.models.Newsletter;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.BaseDbService;
import com.gossamercms.mvc.data.DataSourceManager;


@ModuleDbService
public class NewslettersDbService extends BaseDbService<Newsletter, NewsletterDto> {

    public NewslettersDbService(DataSourceManager ds) {
        super(Newsletter.class, NewsletterDto.class, ds);
    }

    @Override
    protected Newsletter mapToEntity(NewsletterDto dto) {
        return dto.toEntity();
    }

    @Override
    protected NewsletterDto mapToDto(Newsletter entity) {
        return entity.toDto();
    }

    @Override
    protected NewsletterDto removeExcludedFields(NewsletterDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

}
