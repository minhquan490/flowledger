package io.flowledger.platform.query.autoconfigure;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.spi.CriteriaBuilderConfigurationProvider;
import com.blazebit.persistence.view.EntityViewManager;
import io.flowledger.platform.query.QuerySystemException;
import io.flowledger.platform.query.blaze.EntityViewManagerBuilder;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.ServiceLoader;

/**
 * Auto-configuration for Blaze-Persistence infrastructure.
 */
@AutoConfiguration
public class BlazePersistenceAutoConfiguration {

  /**
   * Builds a Blaze {@link CriteriaBuilderFactory} using the SPI configuration provider.
   *
   * @param entityManagerFactory JPA entity manager factory
   * @return criteria builder factory
   */
  @Bean
  @ConditionalOnMissingBean
  public CriteriaBuilderFactory criteriaBuilderFactory(EntityManagerFactory entityManagerFactory) {
    CriteriaBuilderConfiguration configuration = ServiceLoader.load(CriteriaBuilderConfigurationProvider.class)
        .findFirst()
        .orElseThrow(() -> new QuerySystemException(
            "No CriteriaBuilderConfigurationProvider found on the classpath"))
        .createConfiguration();
    return configuration.createCriteriaBuilderFactory(entityManagerFactory);
  }

  /**
   * Builds a Blaze {@link EntityViewManager} using the configured {@link CriteriaBuilderFactory}.
   *
   * @param criteriaBuilderFactory criteria builder factory
   * @param builder entity view manager builder
   * @return entity view manager
   */
  @Bean
  @ConditionalOnMissingBean
  public EntityViewManager entityViewManager(
      CriteriaBuilderFactory criteriaBuilderFactory,
      EntityViewManagerBuilder builder
  ) {
    return builder.build(criteriaBuilderFactory);
  }
}
