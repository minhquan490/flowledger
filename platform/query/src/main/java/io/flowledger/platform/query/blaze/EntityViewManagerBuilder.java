package io.flowledger.platform.query.blaze;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViews;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import io.flowledger.platform.query.QuerySystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collection;
import java.util.List;

/**
 * Builds the Blaze {@link EntityViewManager} using discovered entity views and
 * FlowLedger mapping annotations.
 */
@RequiredArgsConstructor
@Component
public class EntityViewManagerBuilder {
  private final BlazeViewMappingApplier mappingApplier;
  private final BlazeViewLoader blazeViewLoader;
  private final ApplicationContext applicationContext;
  private final ObjectProvider<PlatformTransactionManager> transactionManagerProvider;

  /**
   * Creates an {@link EntityViewManager} configured with discovered entity views.
   *
   * @param cbf the Blaze criteria builder factory
   * @return configured entity view manager
   * @throws QuerySystemException when required bootstrap information is missing
   */
  public EntityViewManager build(CriteriaBuilderFactory cbf) {
    if (cbf == null) {
      throw new QuerySystemException("CriteriaBuilderFactory must not be null");
    }
    if (!AutoConfigurationPackages.has(applicationContext)) {
      throw new QuerySystemException(
          "No auto-configuration base packages found for Blaze view scanning");
    }

    EntityViewConfiguration config = EntityViews.createDefaultConfiguration();
    configureTransactionSupport(config);
    List<String> packagesToScan = AutoConfigurationPackages.get(applicationContext);

    Collection<Class<?>> views = blazeViewLoader.loadViews(packagesToScan.toArray(new String[0]));

    for (Class<?> view : views) {
      mappingApplier.apply(view, config);
    }

    return config.createEntityViewManager(cbf);
  }

  /**
   * Configures Blaze entity-view transaction support via Spring transaction manager when available.
   *
   * @param config the entity-view configuration
   */
  private void configureTransactionSupport(EntityViewConfiguration config) {
    PlatformTransactionManager transactionManager = transactionManagerProvider.getIfAvailable();
    if (transactionManager == null) {
      return;
    }
    config.setTransactionSupport(new BlazeTransactionSupportAdapter(transactionManager));
  }
}
