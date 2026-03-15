package io.flowledger.platform.graphql.application;

import java.util.Map;
import lombok.Getter;

/**
 * Exception that captures validation errors for a GraphQL mutation payload.
 */
@Getter
public class GraphQlValidationException extends GraphQlApiException {

  private final Map<String, String> fieldErrors;

  /**
   * Creates a new validation exception with detailed field errors.
   *
   * @param message the error message
   * @param statusCode the status code
   * @param fieldErrors the validation errors keyed by field name
   */
  public GraphQlValidationException(String message, int statusCode, Map<String, String> fieldErrors) {
    super(message, statusCode);
    this.fieldErrors = fieldErrors;
  }
}
