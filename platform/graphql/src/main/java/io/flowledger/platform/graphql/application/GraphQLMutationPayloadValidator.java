package io.flowledger.platform.graphql.application;

import java.util.Map;

/**
 * Validates GraphQL mutation payloads before execution.
 */
public interface GraphQLMutationPayloadValidator {

  /**
   * Validates the mutation payload.
   *
   * @param payload the mutation payload data
   * @throws GraphQlApiException when the payload is invalid
   */
  void validatePayload(Map<String, Object> payload) throws GraphQlApiException;
}
