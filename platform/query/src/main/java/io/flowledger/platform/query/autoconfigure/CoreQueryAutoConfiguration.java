package io.flowledger.platform.query.autoconfigure;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViews;
import io.flowledger.platform.query.blaze.BlazeMappingResolver;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import io.flowledger.platform.query.blaze.BlazeViewLoader;
import io.flowledger.platform.query.blaze.BlazeViewMappingApplier;
import io.flowledger.platform.query.blaze.EntityViewManagerBuilder;
import io.flowledger.platform.query.mapping.MappingExpressionResolver;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;

/**
 * Auto-configuration entry point for core query infrastructure.
 */
@AutoConfiguration
@ConditionalOnClass({CriteriaBuilderFactory.class, EntityViews.class})
@Import({
    MappingExpressionResolver.class,
    BlazeMappingResolver.class,
    BlazeQueryBuilder.class,
    BlazeViewLoader.class,
    BlazeViewMappingApplier.class,
    EntityViewManagerBuilder.class,
    BlazePersistenceAutoConfiguration.class
})
public class CoreQueryAutoConfiguration {
}
