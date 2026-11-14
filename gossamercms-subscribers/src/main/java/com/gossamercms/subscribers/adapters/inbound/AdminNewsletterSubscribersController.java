package com.gossamercms.subscribers.adapters.inbound;


import com.gossamercms.subscribers.dtos.NewsletterSubscriberDto;
import com.gossamercms.subscribers.dtos.requests.NewsletterSubscriberRequest;
import com.gossamercms.subscribers.dtos.responses.NewsletterSubscriberResponse;
import com.gossamercms.subscribers.handlers.NewsletterSubscribersHandler;
import com.gossamercms.subscribers.models.NewsletterSubscriber;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/newsletter-subscribers")
public class AdminNewsletterSubscribersController extends BaseController<NewsletterSubscriber, NewsletterSubscriberDto, NewsletterSubscriberRequest, NewsletterSubscriberResponse, NewsletterSubscriberResponse> {

    public AdminNewsletterSubscribersController(NewsletterSubscribersHandler handler) {
        super(handler, NewsletterSubscriberResponse.class);
    }

    @Override
    protected NewsletterSubscriberDto toDto(NewsletterSubscriberRequest request) {
        return NewsletterSubscriberDto
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build();
    }

    @Override
    protected NewsletterSubscriberResponse toResponse(NewsletterSubscriberDto dto) {
        return NewsletterSubscriberResponse.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .id(dto.getId())
                .build();
    }
}
