package io.flowledger.platform.graphql.infrastructure.blaze.mutation.validator;

import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;
import org.springframework.stereotype.Component;

import static io.flowledger.platform.graphql.application.GraphQlQueryService.BAD_REQUEST_STATUS;

/**
 * Validates create mutation payload requirements.
 */
@Component
public class CreateGraphQLMutationRequestValidator extends BaseGraphQLMutationRequestValidator {
  /**
   * Ensures create mutations include a data payload.
   *
   * @param request the mutation request
   */
  @Override
  protected void validatePayload(GraphQlMutationRequest request) {
    if (request.data() == null || request.data().isEmpty()) {
      throw new GraphQlApiException("Create mutation must include data payload.", BAD_REQUEST_STATUS);
    }
  }
}
