package io.flowledger.platform.graphql.infrastructure.blaze;

import io.flowledger.platform.graphql.application.AllowAllGraphQlAccessPolicy;
import io.flowledger.platform.graphql.application.GraphQLMutationPayloadValidator;
import io.flowledger.platform.graphql.application.GraphQLMutationPolicy;
import io.flowledger.platform.graphql.application.GraphQlAccessPolicy;
import io.flowledger.platform.graphql.application.NoopGraphQLMutationPayloadValidator;
import io.flowledger.platform.graphql.application.PermitsAllGraphQLMutationPolicy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 * Describes the Blaze view bound to a GraphQL model name.
 */
@Getter
@RequiredArgsConstructor
@Slf4j
public class BlazeGraphQlViewDefinition {

  private final ModelInformation modelInformation;
  private final Class<?> viewClass;
  private final Class<?> entityClass;
  private final ApplicationContext applicationContext;

  private GraphQLMutationPolicy mutationPolicy;
  private GraphQlAccessPolicy accessPolicy;
  private GraphQLMutationPayloadValidator payloadValidator;

  /**
   * Returns the GraphQL model name.
   *
   * @return the model name
   */
  public String model() {
    return modelInformation.model();
  }

  /**
   * Returns the Blaze entity view class.
   *
   * @return the view class
   */
  public Class<?> viewClass() {
    return viewClass;
  }

  /**
   * Returns the JPA entity class.
   *
   * @return the entity class
   */
  public Class<?> entityClass() {
    return entityClass;
  }

  /**
   * Resolves the mutation policy for the model.
   *
   * @return the mutation policy
   */
  @NonNull
  public GraphQLMutationPolicy resolveMutationPolicy() {
    if (mutationPolicy != null) {
      return mutationPolicy;
    }
    Class<? extends GraphQLMutationPolicy> mutationPolicyType = modelInformation.mutationPolicy();
    if (mutationPolicyType == null) {
      log.debug("No mutation policy configured for model {}. Using default policy.", model());
      mutationPolicy = new PermitsAllGraphQLMutationPolicy();
      return mutationPolicy;
    }
    try {
      mutationPolicy = applicationContext.getBean(mutationPolicyType);
      return mutationPolicy;
    } catch (NoSuchBeanDefinitionException _) {
      log.warn("Mutation policy {} not found for model {}. Using default policy.", mutationPolicyType.getName(), model());
      mutationPolicy = new PermitsAllGraphQLMutationPolicy();
      return mutationPolicy;
    }
  }

  /**
   * Resolves the access policy for the model.
   *
   * @return the access policy
   */
  @NonNull
  public GraphQlAccessPolicy resolveAccessPolicy() {
    if (accessPolicy != null) {
      return accessPolicy;
    }
    Class<? extends GraphQlAccessPolicy> accessPolicyType = modelInformation.accessPolicy();
    if (accessPolicyType == null) {
      log.debug("No access policy configured for model {}. Using default policy.", model());
      accessPolicy = new AllowAllGraphQlAccessPolicy();
      return accessPolicy;
    }
    try {
      accessPolicy = applicationContext.getBean(accessPolicyType);
      return accessPolicy;
    } catch (NoSuchBeanDefinitionException _) {
      log.warn("Access policy {} not found for model {}. Using default policy.", accessPolicyType.getName(), model());
      accessPolicy = new AllowAllGraphQlAccessPolicy();
      return accessPolicy;
    }
  }

  /**
   * Resolves the mutation payload validator for the model.
   *
   * @return the payload validator
   */
  @NonNull
  public GraphQLMutationPayloadValidator resolvePayloadValidator() {
    if (payloadValidator != null) {
      return payloadValidator;
    }
    Class<? extends GraphQLMutationPayloadValidator> validatorType = modelInformation.payloadValidator();
    if (validatorType == null) {
      log.debug("No payload validator configured for model {}. Using default validator.", model());
      payloadValidator = new NoopGraphQLMutationPayloadValidator();
      return payloadValidator;
    }
    try {
      payloadValidator = applicationContext.getBean(validatorType);
      return payloadValidator;
    } catch (NoSuchBeanDefinitionException _) {
      log.warn("Payload validator {} not found for model {}. Using default validator.", validatorType.getName(), model());
      payloadValidator = new NoopGraphQLMutationPayloadValidator();
      return payloadValidator;
    }
  }

  /**
   * Captures model metadata for view resolution.
   *
   * @param model the GraphQL model name
   * @param mutationPolicy the mutation policy type
   * @param accessPolicy the access policy type
   * @param payloadValidator the payload validator type
   */
  public record ModelInformation(
      String model,
      Class<? extends GraphQLMutationPolicy> mutationPolicy,
      Class<? extends GraphQlAccessPolicy> accessPolicy,
      Class<? extends GraphQLMutationPayloadValidator> payloadValidator
  ) {
  }
}
