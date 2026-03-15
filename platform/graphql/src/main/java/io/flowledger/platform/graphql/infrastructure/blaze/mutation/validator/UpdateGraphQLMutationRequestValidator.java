package io.flowledger.platform.graphql.infrastructure.blaze.mutation.validator;

import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;
import org.springframework.stereotype.Component;

import static io.flowledger.platform.graphql.application.GraphQlQueryService.BAD_REQUEST_STATUS;

/**
 * Validates update mutation payload requirements.
 */
@Component
public class UpdateGraphQLMutationRequestValidator extends BaseGraphQLMutationRequestValidator {
  /**
   * Ensures update mutations include a key and data payload.
   *
   * @param request the mutation request
   */
  @Override
  protected void validatePayload(GraphQlMutationRequest request) {
    if (request.key() == null || request.key().isEmpty()) {
      throw new GraphQlApiException("Update mutation must include a key.", BAD_REQUEST_STATUS);
    }
    if (request.data() == null || request.data().isEmpty()) {
      throw new GraphQlApiException("Update mutation must include data payload.", BAD_REQUEST_STATUS);
    }
  }
}
