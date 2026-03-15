package io.flowledger.platform.graphql.infrastructure.blaze.mutation;

import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.graphql.application.GraphQlQueryHandlerRegistry;
import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;
import io.flowledger.platform.graphql.domain.GraphQlMutationResult;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlViewDefinition;
import io.flowledger.platform.graphql.infrastructure.blaze.mutation.validator.UpdateGraphQLMutationRequestValidator;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;

import java.util.Map;

import static io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlMutationHandler.UNSUPPORTED;

/**
 * Handles update mutations using Blaze entity views.
 */
public class UpdateGraphQLMutationHandler extends BaseGraphQLMutationHandler {
  private final BlazeGraphQlViewDefinition viewDefinition;
  private final EntityViewManager entityViewManager;
  private final UpdateGraphQLMutationRequestValidator validator;

  /**
   * Creates an update-mutation handler.
   *
   * @param queryBuilder the Blaze query builder
   * @param viewDefinition the view definition for the target model
   * @param entityViewManager the entity view manager
   * @param validator the validator to validate request
   */
  public UpdateGraphQLMutationHandler(
      BlazeQueryBuilder queryBuilder,
      BlazeGraphQlViewDefinition viewDefinition,
      EntityViewManager entityViewManager,
      UpdateGraphQLMutationRequestValidator validator
  ) {
    super(queryBuilder);
    this.viewDefinition = viewDefinition;
    this.entityViewManager = entityViewManager;
    this.validator = validator;
  }

  /**
   * Updates an entity using an updatable Blaze view.
   *
   * @param request the mutation request
   * @return the mutation result
   */
  @Override
  public GraphQlMutationResult mutate(GraphQlMutationRequest request) {
    validator.validate(request, viewDefinition);
    Class<?> viewClass = viewDefinition.viewClass();
    if (!viewClass.isAnnotationPresent(UpdatableEntityView.class)) {
      throw new GraphQlApiException("View " + viewClass.getName() + " is not updatable.", UNSUPPORTED);
    }
    Object id = resolveId(request.key());
    Object view = entityViewManager.find(getEntityManager(), viewClass, id);
    if (view == null) {
      throw new GraphQlApiException("No entity found for model " + request.model() + " with id " + id, GraphQlQueryHandlerRegistry.NOT_FOUND);
    }
    Map<String, Object> payload = mergedPayload(request);
    applyPayload(view, payload);
    entityViewManager.save(getEntityManager(), view);
    return new GraphQlMutationResult(true, view);
  }
}
