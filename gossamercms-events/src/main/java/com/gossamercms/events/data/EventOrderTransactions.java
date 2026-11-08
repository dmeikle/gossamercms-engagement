package com.gossamercms.events.data;

import com.gossamercms.events.models.EventOrder;
import com.gossamercms.mvc.annotations.ModuleDbService;
import com.gossamercms.mvc.data.DataSourceManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

@ModuleDbService
public class EventOrderTransactions {
    private final DataSourceManager sources;

    public EventOrderTransactions(DataSourceManager sources) {
        this.sources = sources;
    }

    public <T> T transaction(Supplier<T> work) {
        // Select the workflow datasource explicitly rather than a host's default transaction manager.
        var jdbc = sources.get(EventOrder.META.datasourceKey()).getNamedParameterJdbcTemplate();
        var manager = new DataSourceTransactionManager(jdbc.getJdbcTemplate().getDataSource());
        var transaction = new TransactionTemplate(manager);
        transaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transaction.execute(status -> work.get());
    }
}
