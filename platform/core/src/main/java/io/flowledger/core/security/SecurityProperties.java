package io.flowledger.core.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configuration properties for application security settings.
 *
 * <p>Maps properties under {@code app.security} to a typed configuration model.
 */
@Component
@ConfigurationProperties(prefix = SecurityProperties.PREFIX)
@Getter
@Setter
@ToString
public class SecurityProperties {

  /**
   * Property prefix for application security configuration.
   */
  public static final String PREFIX = "app.security";

  /**
   * Authentication mode used by the application.
   */
  private AuthMode mode;

  /**
   * Keycloak configuration properties.
   */
  private KeycloakProperties keycloak = new KeycloakProperties();

  /**
   * Cross-origin resource sharing configuration properties.
   */
  private CorsProperties cors = new CorsProperties();

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

  /**
   * Configuration properties for CORS.
   */
  @Getter
  @Setter
  @ToString
  public static class CorsProperties {

    /**
     * Default max age for CORS preflight requests (seconds).
     */
    public static final Long DEFAULT_MAX_AGE_SECONDS = 1800L;

    /**
     * Whether CORS is enabled.
     */
    private boolean enabled = true;

    /**
     * Allowed origins for CORS.
     */
    private List<String> allowedOrigins = List.of();

    /**
     * Allowed HTTP methods for CORS.
     */
    private List<String> allowedMethods = List.of();

    /**
     * Allowed headers for CORS.
     */
    private List<String> allowedHeaders = List.of();

    /**
     * Whether credentials are allowed for CORS.
     */
    private boolean allowCredentials = true;

    /**
     * Max age (seconds) for CORS preflight requests.
     */
    private Long maxAge = DEFAULT_MAX_AGE_SECONDS;
  }
}
