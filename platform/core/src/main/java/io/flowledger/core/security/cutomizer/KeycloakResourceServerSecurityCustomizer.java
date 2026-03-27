package io.flowledger.core.security.cutomizer;

import io.flowledger.core.security.JwtGrantedAuthoritiesConverter;
import io.flowledger.core.security.SecurityCustomizer;
import io.flowledger.core.security.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

/**
 * Configures JWT-based OAuth2 resource server support.
 */
@RequiredArgsConstructor
public class KeycloakResourceServerSecurityCustomizer implements SecurityCustomizer {

  /**
   * Property value that enables Keycloak authentication mode.
   */
  public static final String KEYCLOAK_MODE = "keycloak";

  private final SecurityProperties authProperties;

  /**
   * Applies OAuth2 resource server configuration.
   *
   * @param httpSecurity the HTTP security builder
   */
  @Override
  public void customize(HttpSecurity httpSecurity) {
    httpSecurity.oauth2ResourceServer(oauth2 -> oauth2
        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
  }

  /**
   * Builds a JWT authentication converter that maps Keycloak role claims to granted authorities.
   *
   * @return the JWT authentication converter
   */
  private JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(new JwtGrantedAuthoritiesConverter(authProperties));
    return converter;
  }
}
