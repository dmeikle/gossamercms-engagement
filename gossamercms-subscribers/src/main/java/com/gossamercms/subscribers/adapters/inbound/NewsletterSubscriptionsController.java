package com.gossamercms.subscribers.adapters.inbound;

import com.gossamercms.mvc.controllers.TypedBaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.subscribers.dtos.NewsletterDto;
import com.gossamercms.subscribers.dtos.requests.AddSubscriptionRequest;
import com.gossamercms.subscribers.dtos.requests.NewsletterSubscriptionRequest;
import com.gossamercms.subscribers.dtos.requests.UnsubscribeRequest;
import com.gossamercms.subscribers.dtos.responses.NewsletterSubscriptionListResponse;
import com.gossamercms.subscribers.dtos.responses.NewsletterSubscriptionResponse;
import com.gossamercms.subscribers.enums.SubscriptionStatus;
import com.gossamercms.subscribers.services.SubscriptionService;
import org.springframework.web.bind.annotation.*;
import com.gossamercms.subscribers.models.NewsletterSubscription;
import com.gossamercms.subscribers.dtos.NewsletterSubscriptionDto;
import com.gossamercms.subscribers.handlers.NewsletterSubscriptionsHandler;

import java.time.Instant;
import java.util.Map;


@RestController
@RequestMapping("/newsletter-subscriptions")
public class NewsletterSubscriptionsController extends TypedBaseController<NewsletterSubscription, NewsletterSubscriptionDto, NewsletterSubscriptionRequest, NewsletterSubscriptionResponse, NewsletterSubscriptionResponse> {

    private final SubscriptionService subscriptionService;

    public NewsletterSubscriptionsController(NewsletterSubscriptionsHandler handler,
                                                SubscriptionService subscriptionService) {
        super(handler, NewsletterSubscriptionResponse.class);
        this.subscriptionService = subscriptionService;
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

    @PostMapping("subscribe/{newsletter}")
    public ApiResponse<NewsletterSubscriptionDto> subscribe(
            @PathVariable("newsletter") NewsletterDto newsletter,
            @RequestBody AddSubscriptionRequest request) {

        NewsletterSubscriptionDto subscription = subscriptionService.subscribe(newsletter, request);

        return ApiResponse.ok(subscription);
    }

    @GetMapping("/confirm/{token}")
    public ApiResponse<NewsletterSubscriptionDto> confirmSubscription(@PathVariable("token") String token) {
        NewsletterSubscriptionDto newsletterSubscription = handler.get(
                Map.of("confirmationToken", token));

        NewsletterSubscriptionDto dto = handler.updateById(newsletterSubscription.getSubscriberId(),
                newsletterSubscription.getId(),
                NewsletterSubscriptionDto.builder()
                        .confirmedAt(Instant.now())
                        .status(SubscriptionStatus.ACTIVE.name())
                        .build()
        );

        return ApiResponse.ok(dto);
    }

    @PostMapping("/unsubscribe/{newsletterSubscription}")
    public ApiResponse<NewsletterDto> unsubscribe(@PathVariable("newsletterSubscription") NewsletterSubscriptionDto newsletterSubscription,
                                                 @RequestBody UnsubscribeRequest request) {
        NewsletterDto newsletter = subscriptionService.unsubscribe(newsletterSubscription, request);

        return ApiResponse.ok(newsletter);
    }

    @GetMapping("my-subscriptions")
    public ApiResponse<ListResultset<NewsletterSubscriptionListResponse>> getMySubscriptions(
            @RequestParam("email") String email
    ) {

        return ApiResponse.ok(subscriptionService.getSubscriptionList(email));
    }
}
