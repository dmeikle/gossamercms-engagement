package com.gossamercms.subscribers.adapters.inbound;


import com.gossamercms.subscribers.dtos.NewsletterSubscriptionDto;
import com.gossamercms.subscribers.dtos.requests.NewsletterSubscriptionRequest;
import com.gossamercms.subscribers.dtos.responses.NewsletterSubscriptionResponse;
import com.gossamercms.subscribers.handlers.NewsletterSubscriptionsHandler;
import com.gossamercms.subscribers.models.NewsletterSubscription;
import com.gossamercms.mvc.controllers.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/newsletter-subscriptions")
public class AdminNewsletterSubscriptionsController extends BaseController<NewsletterSubscription, NewsletterSubscriptionDto, NewsletterSubscriptionRequest, NewsletterSubscriptionResponse, NewsletterSubscriptionResponse> {

    public AdminNewsletterSubscriptionsController(NewsletterSubscriptionsHandler handler) {
        super(handler, NewsletterSubscriptionResponse.class);
    }

    @Override
    protected NewsletterSubscriptionDto toDto(NewsletterSubscriptionRequest request) {
        return NewsletterSubscriptionDto.builder()
                .subscriberId(request.getSubscriberId())
                .newsletterId(request.getNewsletterId())
                .status(request.getStatus())
                .consentType(request.getConsentType())
                .consentedAt(request.getConsentedAt())
                .consentSource(request.getConsentSource())
                .consentText(request.getConsentText())
                .subscribedAt(request.getSubscribedAt())
                .confirmedAt(request.getConfirmedAt())
                .unsubscribedAt(request.getUnsubscribedAt())
                .confirmationToken(request.getConfirmationToken())
                .confirmationTokenExpiresAt(request.getConfirmationTokenExpiresAt())
                .ipAddress(request.getIpAddress())
                .userAgent(request.getUserAgent())
                .build();
    }

    @Override
    protected NewsletterSubscriptionResponse toResponse(NewsletterSubscriptionDto dto) {
        return NewsletterSubscriptionResponse.builder()
                .id(dto.getId())
                .subscriberId(dto.getSubscriberId())
                .newsletterId(dto.getNewsletterId())
                .status(dto.getStatus())
                .consentType(dto.getConsentType())
                .consentedAt(dto.getConsentedAt())
                .consentSource(dto.getConsentSource())
                .consentText(dto.getConsentText())
                .subscribedAt(dto.getSubscribedAt())
                .confirmedAt(dto.getConfirmedAt())
                .unsubscribedAt(dto.getUnsubscribedAt())
                .confirmationToken(dto.getConfirmationToken())
                .confirmationTokenExpiresAt(dto.getConfirmationTokenExpiresAt())
                .ipAddress(dto.getIpAddress())
                .userAgent(dto.getUserAgent())
                .build();
    }
}
