package io.flowledger.platform.graphql.application;

import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;

public class PermitsAllGraphQLMutationPolicy implements GraphQLMutationPolicy {
  @Override
  public void validateWriteAccess(GraphQlMutationRequest request) {
    // do not thing
  }
}
