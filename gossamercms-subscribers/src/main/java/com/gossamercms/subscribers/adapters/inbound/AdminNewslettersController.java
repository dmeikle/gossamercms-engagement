package com.gossamercms.subscribers.adapters.inbound;


import com.gossamercms.subscribers.dtos.NewsletterDto;
import com.gossamercms.subscribers.dtos.requests.NewsletterRequest;
import com.gossamercms.subscribers.dtos.responses.NewsletterResponse;
import com.gossamercms.subscribers.handlers.NewslettersHandler;
import com.gossamercms.subscribers.models.Newsletter;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/newsletters")
public class AdminNewslettersController extends BaseController<Newsletter, NewsletterDto, NewsletterRequest, NewsletterResponse, NewsletterResponse> {

    public AdminNewslettersController(NewslettersHandler handler) {
        super(handler, NewsletterResponse.class);
    }

    @Override
    protected NewsletterDto toDto(NewsletterRequest request) {
        return NewsletterDto.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isActive(request.getIsActive())
                .build();
    }

    @Override
    protected NewsletterResponse toResponse(NewsletterDto dto) {
        return NewsletterResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .isActive(dto.getIsActive())
                .build();
    }
}
