package io.flowledger.platform.rbac.infrastructure.autoconfigure;

import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlModelRegistry;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import io.flowledger.platform.rbac.application.NoopRbacSubjectProvider;
import io.flowledger.platform.rbac.application.RbacSubjectProvider;
import io.flowledger.platform.rbac.application.service.RbacFieldPermissionService;
import io.flowledger.platform.rbac.application.service.RbacPermissionService;
import io.flowledger.platform.rbac.application.service.RbacRoleResolver;
import io.flowledger.platform.rbac.application.service.RbacRowFilterService;
import io.flowledger.platform.rbac.infrastructure.blaze.BlazeRbacQueryBuilderExtension;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import io.flowledger.platform.rbac.infrastructure.sync.RbacBootstrapRunner;
import io.flowledger.platform.rbac.infrastructure.sync.RbacResourceSynchronizer;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * Auto-configuration entry point for RBAC query extensions.
 */
@AutoConfiguration
@ConditionalOnClass({BlazeQueryBuilder.class, BlazeGraphQlModelRegistry.class})
@Import({
    BlazeRbacQueryBuilderExtension.class,
    RbacGraphQlAccessPolicy.class,
    RbacGraphQLMutationPolicy.class,
    RbacRowFilterService.class,
    RbacPermissionService.class,
    RbacFieldPermissionService.class,
    RbacRoleResolver.class
})
public class RbacAutoConfiguration {

  /**
   * Provides a default subject provider when none is configured.
   *
   * @return the subject provider
   */
  @Bean
  @ConditionalOnMissingBean
  public RbacSubjectProvider rbacSubjectProvider() {
    return new NoopRbacSubjectProvider();
  }

  /**
   * Creates the RBAC resource synchronizer.
   *
   * @param modelRegistry the GraphQL model registry
   * @param entityManager the entity manager
   * @return the resource synchronizer
   */
  @Bean
  @ConditionalOnBean({BlazeGraphQlModelRegistry.class, EntityManager.class})
  public RbacResourceSynchronizer rbacResourceSynchronizer(
      BlazeGraphQlModelRegistry modelRegistry,
      EntityManager entityManager
  ) {
    return new RbacResourceSynchronizer(modelRegistry, entityManager);
  }

  /**
   * Creates the RBAC bootstrap runner.
   *
   * @param synchronizer the resource synchronizer
   * @return the bootstrap runner
   */
  @Bean
  @ConditionalOnBean(RbacResourceSynchronizer.class)
  public RbacBootstrapRunner rbacBootstrapRunner(RbacResourceSynchronizer synchronizer) {
    return new RbacBootstrapRunner(synchronizer);
  }
}
