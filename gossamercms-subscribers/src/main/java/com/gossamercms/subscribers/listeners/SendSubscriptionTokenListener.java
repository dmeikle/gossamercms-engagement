package com.gossamercms.subscribers.listeners;

import com.gossamercms.notifications.dtos.requests.NotificationRequest;
import com.gossamercms.notifications.services.NotificationJobService;
import com.gossamercms.subscribers.data.NewslettersDbService;
import gossamercms.globals.config.SystemPrincipal;
import com.gossamercms.mvc.annotations.ModuleEventLister;
import com.gossamercms.mvc.data.QueryFilter;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.outbox.dispatcher.GossamerEventListener;
import com.gossamercms.subscribers.dtos.NewsletterSubscriptionDto;
import com.gossamercms.subscribers.events.NewSubScriberEvent;
import com.gossamercms.subscribers.factories.ConfirmationTokenFactory;
import com.gossamercms.subscribers.data.NewsletterSubscriptionsDbService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@ModuleEventLister
public class SendSubscriptionTokenListener
        implements GossamerEventListener<NewSubScriberEvent> {

    private static final Logger log = LoggerFactory.getLogger(SendSubscriptionTokenListener.class);
    private static final String RECIPIENT_TYPE_SUBSCRIBER = "SUBSCRIBER";
    private static final String NOTIFICATION_PRIORITY_NORMAL = "NORMAL";
    static final int MAX_DAYS = 90;
    private static final LocalTime DEFAULT_START_TIME = LocalTime.of(8, 0);
    private static final Duration DAILY_WINDOW = Duration.ofHours(12);

    private final NewsletterSubscriptionsDbService newsletterSubscriptionsDbService;
    private final NotificationJobService notificationJobService;
    private final NewslettersDbService newslettersDbService;


    public SendSubscriptionTokenListener(
            NewsletterSubscriptionsDbService newsletterSubscriptionsDbService,
            NotificationJobService notificationJobService,
            NewslettersDbService newslettersDbService) {
        this.newsletterSubscriptionsDbService = newsletterSubscriptionsDbService;
        this.notificationJobService = notificationJobService;
        this.newslettersDbService = newslettersDbService;
    }

    @Value("${engagement.website-base-url}")
    private String websiteBaseUrl;

    @Override
    public Class<NewSubScriberEvent> eventType() {
        return NewSubScriberEvent.class;
    }

    @EventListener
    @Override
    public void handle(NewSubScriberEvent event) {
        // guard against the same event being delivered more than once
        if (!newsletterSubscriptionsDbService.getAll(QueryOptions.builder()
                .filters(Map.of("id", event.getNewsletterSubscription().getId()))
                .queryFilters(List.of(new QueryFilter("confirmationToken", QueryFilter.Operator.IS_NOT_NULL, null)))
                .build()).list().isEmpty()) {
            log.warn("Token already sent for subscription {}, skipping", event.getNewsletterSubscription().getId());
            return;
        }
        
        NewsletterSubscriptionDto dtoWithToken = newsletterSubscriptionsDbService.updateById(
                SystemPrincipal.ID,
                NewsletterSubscriptionDto.builder()
                        .confirmationToken(ConfirmationTokenFactory.generateToken())
                        .build(),
                event.getNewsletterSubscription().getId());

        String confirmationUrl = UriComponentsBuilder
                .fromUriString(websiteBaseUrl)
                .pathSegment("newsletter-subscriptions", "confirm", "{token}")
                .encode()
                .buildAndExpand(dtoWithToken.getConfirmationToken())
                .toUriString();

        newsletterSubscriptionsDbService.update(SystemPrincipal.ID,
                NewsletterSubscriptionDto.builder()
                        .confirmationToken(dtoWithToken.getConfirmationToken())
                        .build(),
                Map.of("id", event.getNewsletterSubscription().getId()));
        notificationJobService.createReminderJob(
                NotificationRequest.builder()
                        .createdBy(SystemPrincipal.ID)
                        .title("New Subscription Confirmation")
                        .recipientType(RECIPIENT_TYPE_SUBSCRIBER)
                        .recipientId(event.getNewsletterSubscriber().getId())
                        .priority(NOTIFICATION_PRIORITY_NORMAL)
                        .body("You have subscribed to the newsletter "
                                + event.getNewsletter().getName()
                                + ". Please confirm your subscription using the following URL: "
                                + confirmationUrl)
                        .build()

        );

    }
}
