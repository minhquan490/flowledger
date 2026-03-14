package io.flowledger.platform.graphql.application;

import io.flowledger.platform.query.QuerySystemException;

/**
 * Exception for internal GraphQL infrastructure errors.
 */
public class GraphQlInternalException extends QuerySystemException {

  /**
   * Creates a new internal exception.
   *
   * @param message the error message
   */
  public GraphQlInternalException(String message) {
    super(message);
  }
}
