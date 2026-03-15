package io.flowledger.application.identity.config;

/**
 * Cache names used by identity services.
 */
public final class IdentityCacheNames {

  /**
   * Cache storing user identifiers resolved by email.
   */
  public static final String USER_ID_BY_EMAIL = "identity.userIdByEmail";

  private IdentityCacheNames() {
    throw new IllegalStateException("Utility class");
  }
}
