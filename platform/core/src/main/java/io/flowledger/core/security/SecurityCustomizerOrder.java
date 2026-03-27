package io.flowledger.core.security;

/**
 * Defines ordering constants for security customizers.
 */
public final class SecurityCustomizerOrder {

  /**
   * Order for CORS customization.
   */
  public static final int CORS = 10;

  /**
   * Order for CSRF customization.
   */
  public static final int CSRF = 20;

  /**
   * Order for session management customization.
   */
  public static final int SESSION = 30;

  /**
   * Order for authorization customization.
   */
  public static final int AUTHORIZATION = 40;

  /**
   * Order for OAuth2 resource server customization.
   */
  public static final int RESOURCE_SERVER = 50;

  /**
   * Order for exception handling customization.
   */
  public static final int EXCEPTION_HANDLING = 60;

  /**
   * Prevents instantiation.
   */
  private SecurityCustomizerOrder() {
  }
}
