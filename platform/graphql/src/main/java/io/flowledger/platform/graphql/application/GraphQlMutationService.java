package io.flowledger.platform.graphql.application;

import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;
import io.flowledger.platform.graphql.domain.GraphQlMutationResult;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dispatches GraphQL mutation requests to registered handlers.
 */
public class GraphQlMutationService {
  public static final String ACTION_CREATE = "create";
  public static final String ACTION_UPDATE = "update";
  public static final String ACTION_DELETE = "delete";

  private final GraphQlMutationHandlerRegistry handlerRegistry;
  private final List<GraphQlMutationCallback> callbacks;

  /**
   * Creates a mutation service.
   *
   * @param handlerRegistry registry of available handlers
   * @param callbacks callbacks invoked after mutations
   */
  public GraphQlMutationService(
      GraphQlMutationHandlerRegistry handlerRegistry,
      List<GraphQlMutationCallback> callbacks
  ) {
    this.handlerRegistry = handlerRegistry;
    this.callbacks = callbacks == null ? List.of() : callbacks;
  }

  /**
   * Executes a mutation request and invokes callbacks.
   *
   * @param request the mutation request
   * @return the mutation result
   */
  @Transactional
  public GraphQlMutationResult mutate(GraphQlMutationRequest request) {
    GraphQlMutationRequest normalized = normalizeRequest(request);
    GraphQlMutationResult result = handlerRegistry.handlerFor(normalized.model()).mutate(normalized);
    invokeCallbacks(normalized, result);
    return result;
  }

  /**
   * Ensures the mutation request is valid.
   *
   * @param request the incoming request
   * @return the normalized request
   */
  private GraphQlMutationRequest normalizeRequest(GraphQlMutationRequest request) {
    if (request == null) {
      throw new GraphQlApiException("Mutation request must be provided.", GraphQlQueryService.BAD_REQUEST_STATUS);
    }
    if (request.model() == null || request.model().isBlank()) {
      throw new GraphQlApiException("Mutation request must include a model name.", GraphQlQueryService.BAD_REQUEST_STATUS);
    }
    String action = normalizeAction(request.action());
    Map<String, Object> key = request.key() == null ? Map.of() : request.key();
    Map<String, Object> data = request.data() == null ? Map.of() : request.data();
    return new GraphQlMutationRequest(request.model(), action, key, data);
  }

  /**
   * Normalizes and validates the mutation action.
   *
   * @param action the action input
   * @return the normalized action
   */
  private String normalizeAction(String action) {
    if (action == null || action.isBlank()) {
      throw new GraphQlApiException("Mutation request must include an action.", GraphQlQueryService.BAD_REQUEST_STATUS);
    }
    String normalized = action.trim().toLowerCase();
    if (!ACTION_CREATE.equals(normalized)
        && !ACTION_UPDATE.equals(normalized)
        && !ACTION_DELETE.equals(normalized)) {
      throw new GraphQlApiException("Unsupported mutation action: " + action, 422);
    }
    return normalized;
  }

  /**
   * Invokes all registered callbacks.
   *
   * @param request the mutation request
   * @param result the mutation result
   */
  private void invokeCallbacks(GraphQlMutationRequest request, GraphQlMutationResult result) {
    for (GraphQlMutationCallback callback : callbacks) {
      if (callback == null) {
        continue;
      }
      callback.afterMutate(request, result);
    }
  }
}
