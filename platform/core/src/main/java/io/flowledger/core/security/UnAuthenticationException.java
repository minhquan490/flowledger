package io.flowledger.core.security;

/**
 * Exception thrown when authentication details are missing or invalid.
 */
public class UnAuthenticationException extends RuntimeException {

  /**
   * Creates a new unauthenticated exception with a message.
   *
   * @param message the exception message
   */
  public UnAuthenticationException(String message) {
    super(message);
  }

  /**
   * Creates a new unauthenticated exception with a message and cause.
   *
   * @param message the exception message
   * @param cause the root cause
   */
  public UnAuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }
}
