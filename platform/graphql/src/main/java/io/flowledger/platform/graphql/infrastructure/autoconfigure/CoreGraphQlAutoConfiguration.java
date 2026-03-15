package io.flowledger.platform.graphql.infrastructure.autoconfigure;

import graphql.scalars.ExtendedScalars;
import io.flowledger.platform.graphql.application.AllowAllGraphQlAccessPolicy;
import io.flowledger.platform.graphql.application.GraphQlMutationCallback;
import io.flowledger.platform.graphql.application.GraphQlMutationHandlerRegistry;
import io.flowledger.platform.graphql.application.GraphQlMutationService;
import io.flowledger.platform.graphql.application.GraphQlQueryHandlerRegistry;
import io.flowledger.platform.graphql.application.GraphQlQueryService;
import io.flowledger.platform.graphql.application.NoopGraphQLMutationPayloadValidator;
import io.flowledger.platform.graphql.domain.GraphQlMutationHandler;
import io.flowledger.platform.graphql.domain.GraphQlQueryHandler;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlModelRegistry;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlMutationHandler;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlQueryHandler;
import io.flowledger.platform.graphql.infrastructure.blaze.mutation.validator.CreateGraphQLMutationRequestValidator;
import io.flowledger.platform.graphql.infrastructure.blaze.mutation.validator.DeleteGraphQLMutationRequestValidator;
import io.flowledger.platform.graphql.infrastructure.blaze.mutation.validator.UpdateGraphQLMutationRequestValidator;
import io.flowledger.platform.graphql.application.PermitsAllGraphQLMutationPolicy;
import io.flowledger.platform.graphql.infrastructure.web.GraphQlQueryController;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import io.flowledger.platform.query.blaze.BlazeViewLoader;
import com.blazebit.persistence.view.EntityViewManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.execution.GraphQlSource;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.util.List;

/**
 * Autoconfiguration for GraphQL.
 */
@AutoConfiguration
@ConditionalOnClass(GraphQlSource.class)
public class CoreGraphQlAutoConfiguration {

  @Configuration
  @Import({
      CreateGraphQLMutationRequestValidator.class,
      UpdateGraphQLMutationRequestValidator.class,
      DeleteGraphQLMutationRequestValidator.class,
  })
  static class MutationValidatorDeclarator {

  }

  /**
   * Registers core JSON and Long scalar support.
   *
   * @return the runtime wiring configurer
   */
  @Bean
  public RuntimeWiringConfigurer coreRuntimeWiringConfigurer() {
    return wiringBuilder -> wiringBuilder
        .scalar(ExtendedScalars.Json)
        .scalar(ExtendedScalars.GraphQLLong);
  }

  /**
   * Creates the query handler registry.
   *
   * @param handlers the registered handlers
   * @return the handler registry
   */
  @Bean
  public GraphQlQueryHandlerRegistry graphQlQueryHandlerRegistry(List<GraphQlQueryHandler> handlers) {
    return new GraphQlQueryHandlerRegistry(handlers);
  }

  /**
   * Creates the mutation handler registry.
   *
   * @param handlers the registered mutation handlers
   * @return the mutation handler registry
   */
  @Bean
  public GraphQlMutationHandlerRegistry graphQlMutationHandlerRegistry(List<GraphQlMutationHandler> handlers) {
    return new GraphQlMutationHandlerRegistry(handlers);
  }

  /**
   * Creates the query service.
   *
   * @param handlerRegistry the handler registry
   * @return the query service
   */
  @Bean
  public GraphQlQueryService graphQlQueryService(GraphQlQueryHandlerRegistry handlerRegistry) {
    return new GraphQlQueryService(handlerRegistry);
  }

  /**
   * Creates the mutation service.
   *
   * @param handlerRegistry the mutation handler registry
   * @param callbacks mutation callbacks
   * @return the mutation service
   */
  @Bean
  public GraphQlMutationService graphQlMutationService(
      GraphQlMutationHandlerRegistry handlerRegistry,
      List<GraphQlMutationCallback> callbacks
  ) {
    return new GraphQlMutationService(handlerRegistry, callbacks);
  }

  /**
   * Creates the query controller.
   *
   * @param queryService the query service
   * @param mutationService the mutation service
   * @return the controller
   */
  @Bean
  public GraphQlQueryController graphQlQueryController(
      GraphQlQueryService queryService,
      GraphQlMutationService mutationService
  ) {
    return new GraphQlQueryController(queryService, mutationService);
  }

  /**
   * Registers a default access policy when none is provided.
   *
   * @return the default access policy
   */
  @Bean
  @ConditionalOnMissingBean
  public AllowAllGraphQlAccessPolicy graphQlAccessPolicy() {
    return new AllowAllGraphQlAccessPolicy();
  }

  /**
   * Registers a default mutation policy when none is provided.
   *
   * @return the default mutation policy
   */
  @Bean
  @ConditionalOnMissingBean
  public PermitsAllGraphQLMutationPolicy graphQlMutationPolicy() {
    return new  PermitsAllGraphQLMutationPolicy();
  }

  /**
   * Register a default payload validator for mutation operation
   *
   * @return the default validator
   * */
  @Bean
  @ConditionalOnMissingBean
  public NoopGraphQLMutationPayloadValidator noOpGraphQLMutationPayloadValidator() {
    return new NoopGraphQLMutationPayloadValidator();
  }

  /**
   * Creates a registry of Blaze entity views mapped to GraphQL models.
   *
   * @param blazeViewLoader Blaze view loader
   * @param applicationContext Spring application context
   * @return the model registry
   */
  @Bean
  @ConditionalOnBean(BlazeViewLoader.class)
  public BlazeGraphQlModelRegistry blazeGraphQlModelRegistry(
      BlazeViewLoader blazeViewLoader,
      ApplicationContext applicationContext
  ) {
    return new BlazeGraphQlModelRegistry(blazeViewLoader, applicationContext);
  }

  /**
   * Registers the generic Blaze-based query handler.
   *
   * @param modelRegistry the Blaze model registry
   * @param queryBuilder the query builder
   * @param entityViewManager the entity view manager
   * @return the generic query handler
   */
  @Bean
  @ConditionalOnBean({BlazeQueryBuilder.class, EntityViewManager.class, BlazeViewLoader.class})
  public GraphQlQueryHandler blazeGraphQlQueryHandler(
      BlazeGraphQlModelRegistry modelRegistry,
      BlazeQueryBuilder queryBuilder,
      EntityViewManager entityViewManager
  ) {
    return new BlazeGraphQlQueryHandler(
        modelRegistry,
        queryBuilder,
        entityViewManager
    );
  }

  /**
   * Registers the generic Blaze-based mutation handler.
   *
   * @param modelRegistry the Blaze model registry
   * @param queryBuilder the Blaze query builder
   * @param entityViewManager the entity view manager
   * @return the generic mutation handler
   */
  @Bean
  @ConditionalOnBean({BlazeQueryBuilder.class, EntityViewManager.class, BlazeViewLoader.class})
  public GraphQlMutationHandler blazeGraphQlMutationHandler(
      BlazeGraphQlModelRegistry modelRegistry,
      BlazeQueryBuilder queryBuilder,
      EntityViewManager entityViewManager
  ) {
    return new BlazeGraphQlMutationHandler(
        modelRegistry,
        queryBuilder,
        entityViewManager
    );
  }
}
