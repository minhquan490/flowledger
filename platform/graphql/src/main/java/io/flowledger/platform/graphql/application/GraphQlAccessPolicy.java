package io.flowledger.platform.graphql.application;

import io.flowledger.platform.graphql.domain.GraphQlReadRequest;
import io.flowledger.platform.graphql.domain.GraphQlSearchRequest;

/**
 * Applies authorization rules to GraphQL read and search requests.
 *
 * <p>Implementations may validate access, alter filters, or restrict fields
 * based on the current security context.
 */
public interface GraphQlAccessPolicy {

  /**
   * Applies authorization rules to a read request.
   *
   * @param request the incoming read request
   * @return the authorized read request
   */
  GraphQlReadRequest authorizeRead(GraphQlReadRequest request);

  /**
   * Applies authorization rules to a search request.
   *
   * @param request the incoming search request
   * @return the authorized search request
   */
  GraphQlSearchRequest authorizeSearch(GraphQlSearchRequest request);
}
