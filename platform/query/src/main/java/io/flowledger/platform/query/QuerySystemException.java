package io.flowledger.platform.query;

/**
 * Signals a misconfiguration or invalid setup in the query system.
 *
 * <p>This is typically thrown when required metadata is missing on annotations or
 * when query mapping components are misused.
 */
public class QuerySystemException extends RuntimeException {
  public QuerySystemException(String message) {
    super(message);
  }

  public QuerySystemException(String message, Throwable cause) {
    super(message, cause);
  }
}
