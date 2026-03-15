package io.flowledger.platform.graphql.application;

import java.util.Map;

public class NoopGraphQLMutationPayloadValidator implements GraphQLMutationPayloadValidator {

  @Override
  public void validatePayload(Map<String, Object> payload) throws GraphQlApiException {
    // Do nothing
  }
}
