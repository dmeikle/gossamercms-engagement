package com.gossamercms.subscribers.data;

import com.gossamercms.mvc.data.*;
import com.gossamercms.subscribers.dtos.NewsletterSubscriptionDto;
import com.gossamercms.subscribers.dtos.responses.NewsletterSubscriptionListResponse;
import com.gossamercms.subscribers.models.NewsletterSubscription;
import com.gossamercms.mvc.annotations.ModuleDbService;

import java.util.Collections;
import java.util.UUID;


@ModuleDbService
public class NewsletterSubscriptionsDbService extends BaseDbService<NewsletterSubscription, NewsletterSubscriptionDto> {

    public NewsletterSubscriptionsDbService(DataSourceManager ds) {
        super(NewsletterSubscription.class, NewsletterSubscriptionDto.class, ds);
    }

    @Override
    protected NewsletterSubscription mapToEntity(NewsletterSubscriptionDto dto) {
        return dto.toEntity();
    }

    @Override
    protected NewsletterSubscriptionDto mapToDto(NewsletterSubscription entity) {
        return entity.toDto();
    }

    @Override
    protected NewsletterSubscriptionDto removeExcludedFields(NewsletterSubscriptionDto dto) {
        return dto;
    }

    @Override
    protected void throw404(String id) {

    }

    public ListResultset<NewsletterSubscriptionListResponse> getSubscriptionList(QueryOptions options) {
        String sql = """
                select n.id as "newsletterId", n.name as "newsletterName",
                       ns.id as "subscriptionId", ns.status as "subscriptionStatus"
                from newsletter_subscriptions ns
                       inner join newsletters n on n.id = ns."newsletterId"
                       inner join newsletter_subscribers nsub on nsub.id = ns."subscriberId"
                /*WHERE_CLAUSE*/
                """;
        String countSql = """
                select count(*) from newsletter_subscriptions ns
                inner join newsletters n on n.id = ns."newsletterId"
                inner join newsletter_subscribers nsub on nsub.id = ns."subscriberId"
                /*WHERE_CLAUSE*/
                """;
        DataSourceAdapter ds = dsManager.get(meta.datasourceKey());

        return ds.findAllBySql(
                sql,
                countSql,
                options.filters(),  // must contain "providerId" -> UUID
                Collections.emptyMap(),
                Collections.emptyMap(),
                options.page(),
                options.size(),
                (rs, rowNum) -> NewsletterSubscriptionListResponse.builder()
                        .newsletterId(rs.getObject("newsletterId", UUID.class))
                        .newsletterName(rs.getString("newsletterName"))
                        .subscriptionId(rs.getObject("subscriptionId", UUID.class))
                        .subscriptionStatus(rs.getString("subscriptionStatus"))
                        .build()
        );
    }
}
