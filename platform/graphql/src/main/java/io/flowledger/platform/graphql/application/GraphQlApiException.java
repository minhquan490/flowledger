package io.flowledger.platform.graphql.application;

import lombok.Getter;

/**
 * Exception for API-facing GraphQL errors.
 */
@Getter
public class GraphQlApiException extends RuntimeException {
  /**
   *  Returns the status code.
   */
  private final int statusCode;

  /**
   * Creates a new API exception.
   *
   * @param message the error message
   */
  public GraphQlApiException(String message) {
    super(message);
    this.statusCode = 400;
  }

  /**
   * Creates a new API exception with a status code.
   *
   * @param message the error message
   * @param statusCode the status code
   */
  public GraphQlApiException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

}
