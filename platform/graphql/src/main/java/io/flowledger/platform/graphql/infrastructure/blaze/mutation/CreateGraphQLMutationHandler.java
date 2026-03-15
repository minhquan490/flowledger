package io.flowledger.platform.graphql.infrastructure.blaze.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityViewManager;
import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;
import io.flowledger.platform.graphql.domain.GraphQlMutationResult;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlMutationHandler;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlViewDefinition;
import io.flowledger.platform.graphql.infrastructure.blaze.mutation.validator.CreateGraphQLMutationRequestValidator;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;

import java.util.Map;

/**
 * Handles create mutations using Blaze entity views.
 */
public class CreateGraphQLMutationHandler extends BaseGraphQLMutationHandler {
  private final BlazeGraphQlViewDefinition viewDefinition;
  private final EntityViewManager entityViewManager;
  private final CreateGraphQLMutationRequestValidator requestValidator;

  /**
   * Creates a create-mutation handler.
   *
   * @param queryBuilder the Blaze query builder
   * @param viewDefinition the view definition for the target model
   * @param entityViewManager the entity view manager
   * @param requestValidator the validator for validate request
   */
  public CreateGraphQLMutationHandler(
      BlazeQueryBuilder queryBuilder,
      BlazeGraphQlViewDefinition viewDefinition,
      EntityViewManager entityViewManager, CreateGraphQLMutationRequestValidator requestValidator
  ) {
    super(queryBuilder);
    this.viewDefinition = viewDefinition;
    this.entityViewManager = entityViewManager;
    this.requestValidator = requestValidator;
  }

  /**
   * Creates a new entity using a creatable Blaze view.
   *
   * @param request the mutation request
   * @return the mutation result
   */
  @Override
  public GraphQlMutationResult mutate(GraphQlMutationRequest request) {
    requestValidator.validate(request, viewDefinition);
    Class<?> viewClass = viewDefinition.viewClass();
    if (!viewClass.isAnnotationPresent(CreatableEntityView.class)) {
      throw new GraphQlApiException("View " + viewClass.getName() + " is not creatable.", BlazeGraphQlMutationHandler.UNSUPPORTED);
    }
    Object view = entityViewManager.create(viewClass);
    Map<String, Object> payload = mergedPayload(request);
    applyPayload(view, payload);
    entityViewManager.save(getEntityManager(), view);
    return new GraphQlMutationResult(true, view);
  }
}
