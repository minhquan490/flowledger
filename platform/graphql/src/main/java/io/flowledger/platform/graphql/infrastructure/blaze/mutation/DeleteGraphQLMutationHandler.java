package io.flowledger.platform.graphql.infrastructure.blaze.mutation;

import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.graphql.application.GraphQlQueryHandlerRegistry;
import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;
import io.flowledger.platform.graphql.domain.GraphQlMutationResult;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlViewDefinition;
import io.flowledger.platform.graphql.infrastructure.blaze.mutation.validator.DeleteGraphQLMutationRequestValidator;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;

/**
 * Handles delete mutations using Blaze entity views.
 */
public class DeleteGraphQLMutationHandler extends BaseGraphQLMutationHandler {
  private final BlazeGraphQlViewDefinition viewDefinition;
  private final DeleteGraphQLMutationRequestValidator deleteGraphQLMutationRequestValidator;

  /**
   * Creates a delete-mutation handler.
   *
   * @param queryBuilder the Blaze query builder
   * @param viewDefinition the view definition for the target model
   * @param deleteGraphQLMutationRequestValidator the validator for validate request
   */
  public DeleteGraphQLMutationHandler(
      BlazeQueryBuilder queryBuilder,
      BlazeGraphQlViewDefinition viewDefinition,
      DeleteGraphQLMutationRequestValidator deleteGraphQLMutationRequestValidator
  ) {
    super(queryBuilder);
    this.viewDefinition = viewDefinition;
    this.deleteGraphQLMutationRequestValidator = deleteGraphQLMutationRequestValidator;
  }

  /**
   * Deletes an entity using the provided identifier key.
   *
   * @param request the mutation request
   * @return the mutation result
   */
  @Override
  public GraphQlMutationResult mutate(GraphQlMutationRequest request) {
    deleteGraphQLMutationRequestValidator.validate(request, viewDefinition);
    Object id = resolveId(request.key());
    Object entity = getEntityManager().find(viewDefinition.entityClass(), id);
    if (entity == null) {
      throw new GraphQlApiException("No entity found for model " + request.model() + " with id " + id, GraphQlQueryHandlerRegistry.NOT_FOUND);
    }
    getEntityManager().remove(entity);
    return new GraphQlMutationResult(true, null);
  }
}
