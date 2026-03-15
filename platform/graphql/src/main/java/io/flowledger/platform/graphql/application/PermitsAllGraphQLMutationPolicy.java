package io.flowledger.platform.graphql.application;

import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;

public class PermitsAllGraphQLMutationPolicy implements GraphQLMutationPolicy {
  @Override
  public void validateWriteAccess(String resource, GraphQlMutationRequest request) {
    // do not thing
  }
}
