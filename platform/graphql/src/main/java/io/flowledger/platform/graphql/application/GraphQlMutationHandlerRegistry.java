package io.flowledger.platform.graphql.application;

import io.flowledger.platform.graphql.domain.GraphQlMutationHandler;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlMutationHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Registry for GraphQL mutation handlers.
 */
public class GraphQlMutationHandlerRegistry {
  private final Map<String, GraphQlMutationHandler> handlersByModel;
  private final GraphQlMutationHandler wildcardHandler;

  /**
   * Creates a registry from the available handlers.
   *
   * @param handlers the registered handlers
   */
  public GraphQlMutationHandlerRegistry(List<GraphQlMutationHandler> handlers) {
    this.handlersByModel = handlers == null
        ? Map.of()
        : handlers.stream()
            .collect(Collectors.toUnmodifiableMap(
                GraphQlMutationHandler::model,
                Function.identity()
            ));
    this.wildcardHandler = handlersByModel.get(BlazeGraphQlMutationHandler.MODEL_WILDCARD);
  }

  /**
   * Returns the handler for the given model name.
   *
   * @param model the model name
   * @return the mutation handler
   */
  public GraphQlMutationHandler handlerFor(String model) {
    GraphQlMutationHandler handler = handlersByModel.get(model);
    if (handler != null) {
      return handler;
    }
    if (wildcardHandler != null) {
      return wildcardHandler;
    }
    throw new GraphQlApiException("No mutation handler registered for model: " + model, GraphQlQueryHandlerRegistry.NOT_FOUND);
  }
}
