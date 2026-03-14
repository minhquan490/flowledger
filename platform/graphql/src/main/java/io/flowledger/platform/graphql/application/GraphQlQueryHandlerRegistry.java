package io.flowledger.platform.graphql.application;

import io.flowledger.platform.graphql.domain.GraphQlQueryHandler;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlMutationHandler;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Registry for GraphQL query handlers keyed by model name.
 *
 * <p>If a model-specific handler is missing, the generic handler registered under
 * {@link #GENERIC_MODEL} will be used.
 */
public class GraphQlQueryHandlerRegistry {
  /**
   * Wildcard model name used by the generic handler.
   */
  public static final String GENERIC_MODEL = BlazeGraphQlMutationHandler.MODEL_WILDCARD;
  public static final int NOT_FOUND = 404;
  private static final String DUPLICATE_HANDLER_MESSAGE = "Multiple handlers registered for model: %s";
  private static final String MISSING_HANDLER_MESSAGE = "No handler registered for model: %s";

  private final Map<String, GraphQlQueryHandler> handlersByModel;
  private final GraphQlQueryHandler genericHandler;

  /**
   * Creates a registry for the provided handlers.
   *
   * @param handlers handlers discovered in the application context
   */
  public GraphQlQueryHandlerRegistry(List<GraphQlQueryHandler> handlers) {
    this.handlersByModel = handlers.stream()
        .collect(Collectors.toUnmodifiableMap(this::normalizeKey, Function.identity(), this::rejectDuplicates));
    this.genericHandler = handlersByModel.get(GENERIC_MODEL);
  }

  /**
   * Returns the handler for the given model name.
   *
   * @param model the model name
   * @return the matching handler
   */
  public GraphQlQueryHandler handlerFor(String model) {
    GraphQlQueryHandler handler = handlersByModel.get(normalizeKey(model));
    if (handler == null && genericHandler != null) {
      return genericHandler;
    }
    if (handler == null) {
      throw new GraphQlApiException(String.format(MISSING_HANDLER_MESSAGE, model), NOT_FOUND);
    }
    return handler;
  }

  /**
   * Normalizes model keys for case-insensitive lookup.
   *
   * @param handler the handler to normalize
   * @return the normalized key
   */
  private String normalizeKey(GraphQlQueryHandler handler) {
    return normalizeKey(handler.model());
  }

  /**
   * Normalizes a model name.
   *
   * @param model the model name
   * @return the normalized key
   */
  private String normalizeKey(String model) {
    if (model == null || model.isBlank()) {
      throw new GraphQlApiException("Model name must be provided.", GraphQlQueryService.BAD_REQUEST_STATUS);
    }
    return model.toLowerCase(Locale.ROOT).trim();
  }

  /**
   * Prevents duplicate handler registrations.
   *
   * @param existing the existing handler
   * @param duplicate the duplicate handler
   * @return never returns
   */
  private GraphQlQueryHandler rejectDuplicates(GraphQlQueryHandler existing, GraphQlQueryHandler duplicate) {
    throw new GraphQlInternalException(String.format(DUPLICATE_HANDLER_MESSAGE, existing.model()));
  }
}
