package io.flowledger.platform.graphql.infrastructure.blaze.mutation.validator;

import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;
import org.springframework.stereotype.Component;

import static io.flowledger.platform.graphql.application.GraphQlQueryService.BAD_REQUEST_STATUS;

/**
 * Validates delete mutation payload requirements.
 */
@Component
public class DeleteGraphQLMutationRequestValidator extends BaseGraphQLMutationRequestValidator {
  /**
   * Ensures delete mutations include a key.
   *
   * @param request the mutation request
   */
  @Override
  protected void validatePayload(GraphQlMutationRequest request) {
    if (request.key() == null || request.key().isEmpty()) {
      throw new GraphQlApiException("Delete mutation must include a key.", BAD_REQUEST_STATUS);
    }
  }
}
