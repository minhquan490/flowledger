package io.flowledger.core.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for application authentication settings.
 *
 * <p>Maps properties under {@code app.auth} to a typed configuration model.
 */
@Component
@ConfigurationProperties(prefix = AppAuthProperties.PREFIX)
@Getter
@Setter
@ToString
public class AppAuthProperties {

  /**
   * Property prefix for application authentication configuration.
   */
  public static final String PREFIX = "app.auth";

  /**
   * Authentication mode used by the application.
   */
  private AuthMode mode;

  /**
   * Keycloak configuration properties.
   */
  private KeycloakProperties keycloak = new KeycloakProperties();

  /**
   * Supported authentication modes.
   */
  public enum AuthMode {
    /**
     * Use Keycloak as the authentication provider.
     */
    KEYCLOAK
  }

  /**
   * Configuration properties for Keycloak integration.
   */
  @Getter
  @Setter
  @ToString
  public static class KeycloakProperties {

    /**
     * Keycloak realm name.
     */
    private String realm;

    /**
     * Keycloak auth server base URL.
     */
    private String authServerUrl;

    /**
     * Keycloak client identifier.
     */
    private String clientId;

    /**
     * Keycloak client secret.
     */
    private String clientSecret;

    /**
     * Keycloak issuer URI.
     */
    private String issuerUri;
  }
}
