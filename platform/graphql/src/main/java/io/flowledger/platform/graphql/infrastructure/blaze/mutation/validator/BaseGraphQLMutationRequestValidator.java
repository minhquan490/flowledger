package io.flowledger.platform.graphql.infrastructure.blaze.mutation.validator;

import io.flowledger.platform.graphql.application.GraphQLMutationPayloadValidator;
import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlViewDefinition;

import java.util.Map;

import static io.flowledger.platform.graphql.application.GraphQlQueryService.BAD_REQUEST_STATUS;

/**
 * Base validator that enforces common mutation request requirements.
 */
public abstract class BaseGraphQLMutationRequestValidator {

  /**
   * Validates a mutation request before execution.
   *
   * @param request the mutation request
   * @throws GraphQlApiException when the request is invalid
   */
  public final void validate(GraphQlMutationRequest request, BlazeGraphQlViewDefinition viewDefinition) {
    if (request == null) {
      throw new GraphQlApiException("Mutation request must be provided.", BAD_REQUEST_STATUS);
    }
    if (request.model() == null || request.model().isBlank()) {
      throw new GraphQlApiException("Mutation request must include a model name.", BAD_REQUEST_STATUS);
    }
    if (request.action() == null || request.action().isBlank()) {
      throw new GraphQlApiException("Mutation request must include an action.", BAD_REQUEST_STATUS);
    }
    validatePayload(request);

    GraphQLMutationPayloadValidator payloadValidator = viewDefinition.resolvePayloadValidator();
    Map<String, Object> payload = request.data();
    payloadValidator.validatePayload(payload);
  }

  /**
   * Validates action-specific payload rules.
   *
   * @param request the mutation request
   */
  protected abstract void validatePayload(GraphQlMutationRequest request);
}
