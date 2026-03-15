package io.flowledger.platform.graphql.infrastructure.blaze;

import com.blazebit.persistence.view.EntityViewManager;
import io.flowledger.platform.graphql.application.GraphQLMutationPolicy;
import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.graphql.application.GraphQlMutationService;
import io.flowledger.platform.graphql.domain.GraphQlMutationHandler;
import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;
import io.flowledger.platform.graphql.domain.GraphQlMutationResult;
import io.flowledger.platform.graphql.infrastructure.blaze.mutation.CreateGraphQLMutationHandler;
import io.flowledger.platform.graphql.infrastructure.blaze.mutation.DeleteGraphQLMutationHandler;
import io.flowledger.platform.graphql.infrastructure.blaze.mutation.UpdateGraphQLMutationHandler;
import io.flowledger.platform.graphql.infrastructure.blaze.mutation.validator.CreateGraphQLMutationRequestValidator;
import io.flowledger.platform.graphql.infrastructure.blaze.mutation.validator.DeleteGraphQLMutationRequestValidator;
import io.flowledger.platform.graphql.infrastructure.blaze.mutation.validator.UpdateGraphQLMutationRequestValidator;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Generic GraphQL mutation handler backed by Blaze entity views.
 */
@RequiredArgsConstructor
public class BlazeGraphQlMutationHandler implements GraphQlMutationHandler {
  public static final String MODEL_WILDCARD = "*";
  public static final int UNSUPPORTED = 422;

  private final BlazeGraphQlModelRegistry modelRegistry;
  private final BlazeQueryBuilder queryBuilder;
  private final EntityViewManager entityViewManager;

  @Setter(onMethod_ = @Autowired)
  private CreateGraphQLMutationRequestValidator createGraphQLMutationRequestValidator;

  @Setter(onMethod_ = @Autowired)
  private UpdateGraphQLMutationRequestValidator updateGraphQLMutationRequestValidator;

  @Setter(onMethod_ = @Autowired)
  private DeleteGraphQLMutationRequestValidator deleteGraphQLMutationRequestValidator;

  /**
   * Returns the wildcard model name used by this generic handler.
   *
   * @return the wildcard model name
   */
  @Override
  public String model() {
    return MODEL_WILDCARD;
  }

  /**
   * Executes a mutation request using Blaze entity views.
   *
   * @param request the mutation request
   * @return the mutation result
   */
  @Override
  public GraphQlMutationResult mutate(GraphQlMutationRequest request) {
    BlazeGraphQlViewDefinition definition = modelRegistry.viewFor(request.model());
    GraphQLMutationPolicy mutationPolicy = definition.resolveMutationPolicy();
    mutationPolicy.validateWriteAccess(definition.model(), request);
    return switch (request.action()) {
      case GraphQlMutationService.ACTION_CREATE -> create(definition, request);
      case GraphQlMutationService.ACTION_UPDATE -> update(definition, request);
      case GraphQlMutationService.ACTION_DELETE -> delete(definition, request);
      default -> throw new GraphQlApiException("Unsupported mutation action: " + request.action(), UNSUPPORTED);
    };
  }

  /**
   * Creates a new entity via a creatable Blaze view.
   *
   * @param definition the view definition
   * @param request the mutation request
   * @return the mutation result
   */
  private GraphQlMutationResult create(BlazeGraphQlViewDefinition definition, GraphQlMutationRequest request) {
    CreateGraphQLMutationHandler handler = new CreateGraphQLMutationHandler(queryBuilder, definition, entityViewManager, createGraphQLMutationRequestValidator);
    return handler.mutate(request);
  }

  /**
   * Updates an existing entity via an updatable Blaze view.
   *
   * @param definition the view definition
   * @param request the mutation request
   * @return the mutation result
   */
  private GraphQlMutationResult update(BlazeGraphQlViewDefinition definition, GraphQlMutationRequest request) {
    UpdateGraphQLMutationHandler handler = new UpdateGraphQLMutationHandler(queryBuilder, definition, entityViewManager, updateGraphQLMutationRequestValidator);
    return handler.mutate(request);
  }

  /**
   * Deletes an entity by identifier.
   *
   * @param definition the view definition
   * @param request the mutation request
   * @return the mutation result
   */
  private GraphQlMutationResult delete(BlazeGraphQlViewDefinition definition, GraphQlMutationRequest request) {
    DeleteGraphQLMutationHandler deleteHandler = new DeleteGraphQLMutationHandler(queryBuilder, definition, deleteGraphQLMutationRequestValidator);
    return deleteHandler.mutate(request);
  }
}
