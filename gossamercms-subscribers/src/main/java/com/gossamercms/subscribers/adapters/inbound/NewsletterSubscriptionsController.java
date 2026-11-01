package com.gossamercms.subscribers.adapters.inbound;

import com.gossamercms.mvc.controllers.BaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.http.ApiResponse;
import com.gossamercms.subscribers.dtos.NewsletterDto;
import com.gossamercms.subscribers.dtos.requests.AddSubscriptionRequest;
import com.gossamercms.subscribers.dtos.requests.UnsubscribeRequest;
import com.gossamercms.subscribers.dtos.responses.NewsletterSubscriptionListResponse;
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
public class NewsletterSubscriptionsController extends BaseController<NewsletterSubscription, NewsletterSubscriptionDto> {

    private final SubscriptionService subscriptionService;

    public NewsletterSubscriptionsController(NewsletterSubscriptionsHandler handler,
                                                SubscriptionService subscriptionService) {
        super(handler);
        this.subscriptionService = subscriptionService;
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
