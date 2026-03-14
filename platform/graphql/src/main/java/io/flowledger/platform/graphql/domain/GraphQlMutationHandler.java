package io.flowledger.platform.graphql.domain;

/**
 * Handles generic GraphQL mutation requests for a model.
 */
public interface GraphQlMutationHandler {

  /**
   * Returns the model name this handler supports.
   *
   * @return the model name
   */
  String model();

  /**
   * Executes a mutation request.
   *
   * @param request the mutation request
   * @return the mutation result
   */
  GraphQlMutationResult mutate(GraphQlMutationRequest request);
}
