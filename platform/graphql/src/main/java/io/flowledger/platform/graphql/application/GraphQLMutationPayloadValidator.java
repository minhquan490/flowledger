package io.flowledger.platform.graphql.application;

import java.util.Map;

public interface GraphQLMutationPayloadValidator {
  void validatePayload(Map<String, Object> payload) throws GraphQlApiException;
}
