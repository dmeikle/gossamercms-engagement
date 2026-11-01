package com.gossamercms.subscribers.services;


import com.gossamercms.mvc.annotations.ModuleService;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryFilter;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.subscribers.dtos.NewsletterDto;
import com.gossamercms.subscribers.dtos.NewsletterSubscriberDto;
import com.gossamercms.subscribers.dtos.requests.AddSubscriptionRequest;
import com.gossamercms.subscribers.dtos.requests.UnsubscribeRequest;
import com.gossamercms.subscribers.dtos.responses.NewsletterSubscriptionListResponse;
import com.gossamercms.subscribers.enums.SubscriptionStatus;
import com.gossamercms.subscribers.events.NewSubScriberEvent;
import gossamercms.globals.config.SystemPrincipal;
import com.gossamercms.subscribers.data.NewslettersDbService;
import lombok.RequiredArgsConstructor;
import com.gossamercms.subscribers.data.NewsletterSubscriptionsDbService;
import com.gossamercms.subscribers.data.NewsletterSubscribersDbService;
import com.gossamercms.subscribers.dtos.NewsletterSubscriptionDto;
import com.gossamercms.outbox.services.OutboxService;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@ModuleService
@RequiredArgsConstructor
public class SubscriptionService {

    private final NewslettersDbService newslettersDbService;
    private final NewsletterSubscriptionsDbService newsletterSubscriptionsDbService;
    private final NewsletterSubscribersDbService newsletterSubscribersDbService;
    private final OutboxService outboxService;

    public NewsletterSubscriptionDto subscribe(NewsletterDto newsletter, AddSubscriptionRequest request) {

        NewsletterSubscriberDto subscriber = getSubscriber(request);
        // Add the subscriber to the newsletter
         NewsletterSubscriptionDto newsletterSubscription = newsletterSubscriptionsDbService.create(SystemPrincipal.ID,
                NewsletterSubscriptionDto.builder()
                        .newsletterId(newsletter.getId())
                        .ipAddress(request.getIpAddress())
                        .subscribedAt(Instant.now())
                        .subscriberId(subscriber.getId())
                        .consentedAt(request.isConsented() ? Instant.now() : null)
                        .status(SubscriptionStatus.PENDING.name())
                        .userAgent(request.getUserAgent())
                        .build());

         outboxService.publish(
                 "newsletter.subscription.created",
                 newsletterSubscription.getId(),
                 NewSubScriberEvent.builder()
                         .newsletter(newsletter)
                         .newsletterSubscription(newsletterSubscription)
                         .newsletterSubscriber(subscriber)
                         .build()
         );

        return newsletterSubscription;
    }

    private NewsletterSubscriberDto getSubscriber(AddSubscriptionRequest request) {
        NewsletterSubscriberDto subscriber = null;
        try{
            subscriber = newsletterSubscribersDbService.get(
                    Map.of("email", request.getEmail())
            );
        } catch (Exception e) {
            // Handle the exception if needed
        }

        if(subscriber == null) {
            // Create a new subscriber if not found
            subscriber = newsletterSubscribersDbService.create(
                    SystemPrincipal.ID,
                    NewsletterSubscriberDto.builder()
                            .email(request.getEmail())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .build()
            );
        }

        return subscriber;
    }

   public NewsletterDto unsubscribe(NewsletterSubscriptionDto newsletterSubscription, UnsubscribeRequest request) {
        NewsletterSubscriberDto subscriber = newsletterSubscribersDbService.get(
                Map.of("email", request.getEmail())
        );
        NewsletterDto newsletter = newslettersDbService.getById(newsletterSubscription.getNewsletterId());
        
        newsletterSubscriptionsDbService.update(SystemPrincipal.ID,
                NewsletterSubscriptionDto.builder()
                        .status(SubscriptionStatus.UNSUBSCRIBED.name())
                        .unsubscribedAt(Instant.now())
                        .build(),
                Map.of("id", newsletterSubscription.getId(),
                        "subscriberId", subscriber.getId()));

        newsletterSubscriptionsDbService.delete(SystemPrincipal.ID, Map.of("id", newsletterSubscription.getId()));
    
        return newsletter;
    }
    
    public ListResultset<NewsletterSubscriptionListResponse> getSubscriptionList(String email) {
        ListResultset<NewsletterSubscriptionListResponse> result =
                newsletterSubscriptionsDbService.getSubscriptionList(QueryOptions.builder()
                        .filters(Map.of("nsub.email", email))
                                .queryFilters(List.of(
                                        new QueryFilter("consentedAt", QueryFilter.Operator.IS_NOT_NULL, null)
                                ))
                        .build());
        return result;
    }
}
