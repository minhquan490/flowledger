package io.flowledger.platform.graphql.domain;

import io.flowledger.platform.graphql.application.AllowAllGraphQlAccessPolicy;
import io.flowledger.platform.graphql.application.GraphQLMutationPayloadValidator;
import io.flowledger.platform.graphql.application.GraphQLMutationPolicy;
import io.flowledger.platform.graphql.application.GraphQlAccessPolicy;
import io.flowledger.platform.graphql.application.NoopGraphQLMutationPayloadValidator;
import io.flowledger.platform.graphql.application.PermitsAllGraphQLMutationPolicy;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares the GraphQL model name for a Blaze entity view.
 *
 * <p>Example usage:
 *
 * <pre>
 * {@code
 * @EntityView(AccountEntity.class)
 * @GraphQlModel(
 *     value = "account",
 *     mutationPolicy = AccountMutationPolicy.class,
 *     accessPolicy = AccountAccessPolicy.class,
 *     mutationPayloadValidator = AccountPayloadValidator.class
 * )
 * public interface AccountView {
 *   String getId();
 *   String getName();
 * }
 * }
 * </pre>
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface GraphQlModel {

  /**
   * Returns the model name (resource name) used in GraphQL requests.
   *
   * @return the model name
   */
  String value();

  /**
   * Returns the mutation policy class applied to this model.
   *
   * @apiNote mutation policy type must be a spring bean.
   * @return the mutation policy class
   */
  Class<? extends GraphQLMutationPolicy> mutationPolicy() default PermitsAllGraphQLMutationPolicy.class;

  /**
   * Returns the access policy class applied to this model.
   *
   * @apiNote access policy type must be a spring bean.
   * @return the access policy class
   */
  Class<? extends GraphQlAccessPolicy> accessPolicy() default AllowAllGraphQlAccessPolicy.class;

  /**
   * Returns the mutation payload validator class applied to this model.
   *
   * @apiNote mutation payload validator must be a spring bean.
   * @return the mutation payload validator class
   */
  Class<? extends GraphQLMutationPayloadValidator> mutationPayloadValidator() default NoopGraphQLMutationPayloadValidator.class;
}
