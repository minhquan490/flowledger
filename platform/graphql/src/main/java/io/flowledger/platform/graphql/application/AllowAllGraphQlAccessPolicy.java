package io.flowledger.platform.graphql.application;

import io.flowledger.platform.graphql.domain.GraphQlReadRequest;
import io.flowledger.platform.graphql.domain.GraphQlSearchRequest;
import lombok.NoArgsConstructor;

/**
 * Default access policy that allows all requests unchanged.
 */
@NoArgsConstructor
public class AllowAllGraphQlAccessPolicy implements GraphQlAccessPolicy {

  /**
   * Returns the read request unchanged.
   *
   * @param request the incoming read request
   * @return the same read request
   */
  @Override
  public GraphQlReadRequest authorizeRead(GraphQlReadRequest request) {
    return request;
  }

  /**
   * Returns the search request unchanged.
   *
   * @param request the incoming search request
   * @return the same search request
   */
  @Override
  public GraphQlSearchRequest authorizeSearch(GraphQlSearchRequest request) {
    return request;
  }
}
