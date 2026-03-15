package io.flowledger.platform.graphql.application;

import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;
import io.flowledger.platform.graphql.domain.GraphQlMutationResult;

/**
 * Callback invoked after a mutation is executed.
 */
public interface GraphQlMutationCallback {

  /**
   * Executes after a mutation completes successfully or fails.
   *
   * @param request the mutation request
   * @param result the mutation result
   */
  void afterMutate(GraphQlMutationRequest request, GraphQlMutationResult result);

  /**
   * Determines whether this callback should apply to the given GraphQL model or resource name.
   *
   * @param resourceOrModelName the GraphQL model or resource name
   * @return true when the callback should be applied
   */
  boolean canApply(String resourceOrModelName);
}
