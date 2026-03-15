package io.flowledger.platform.graphql.application;

import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;

/**
 * Defines a policy that validates write access for GraphQL mutations.
 */
public interface GraphQLMutationPolicy {

  /**
   * Validates whether the caller can perform the mutation request.
   *
   * @param resource the resource name defined in {@link io.flowledger.platform.graphql.domain.GraphQlModel#value()}
   * @param request the mutation request
   */
  void validateWriteAccess(String resource, GraphQlMutationRequest request);
}
