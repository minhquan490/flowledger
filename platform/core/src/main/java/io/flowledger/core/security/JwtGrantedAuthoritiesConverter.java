package io.flowledger.core.security;

import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Converts Keycloak JWT role claims into Spring Security granted authorities.
 */
public class JwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

  private static final String REALM_ACCESS = "realm_access";
  private static final String RESOURCE_ACCESS = "resource_access";
  private static final String ROLES = "roles";
  private static final String ROLE_PREFIX = "ROLE_";

  private final SecurityProperties authProperties;

  /**
   * Creates a converter that uses application authentication properties to resolve client roles.
   *
   * @param authProperties the application authentication properties
   */
  public JwtGrantedAuthoritiesConverter(SecurityProperties authProperties) {
    this.authProperties = authProperties;
  }

  /**
   * Converts a JWT into Spring Security granted authorities based on Keycloak role claims.
   *
   * @param jwt the JWT to convert
   * @return the granted authorities derived from the token
   */
  @Override
  public List<GrantedAuthority> convert(@NonNull Jwt jwt) {
    Set<String> roles = new LinkedHashSet<>();
    roles.addAll(extractRealmRoles(jwt));
    roles.addAll(extractClientRoles(jwt));
    return roles.stream()
        .map(role -> (GrantedAuthority) () -> ROLE_PREFIX + role.toUpperCase())
        .toList();
  }

  /**
   * Extracts realm roles from the JWT.
   *
   * @param jwt the JWT token
   * @return the realm roles, or an empty collection when none exist
   */
  private Collection<String> extractRealmRoles(Jwt jwt) {
    return extractRoleClaim(jwt);
  }

  /**
   * Extracts client roles for the configured Keycloak client from the JWT.
   *
   * @param jwt the JWT token
   * @return the client roles, or an empty collection when none exist
   */
  private Collection<String> extractClientRoles(Jwt jwt) {
    String clientId = authProperties.getKeycloak().getClientId();
    if (clientId == null || clientId.isBlank()) {
      return List.of();
    }
    Map<String, Object> resourceAccess = jwt.getClaim(RESOURCE_ACCESS);
    if (resourceAccess == null) {
      return List.of();
    }
    Object clientAccess = resourceAccess.get(clientId);
    if (!(clientAccess instanceof Map<?, ?> clientMap)) {
      return List.of();
    }
    Object roles = clientMap.get(ROLES);
    return coerceRoles(roles);
  }

  /**
   * Extracts a role claim from the JWT.
   *
   * @param jwt the JWT token
   * @return the roles, or an empty collection when none exist
   */
  private Collection<String> extractRoleClaim(Jwt jwt) {
    Map<String, Object> access = jwt.getClaim(REALM_ACCESS);
    if (access == null) {
      return List.of();
    }
    Object roles = access.get(ROLES);
    return coerceRoles(roles);
  }

  /**
   * Coerces an arbitrary roles object into a collection of non-blank role strings.
   *
   * @param roles the raw roles value
   * @return the filtered roles
   */
  private Collection<String> coerceRoles(Object roles) {
    if (roles instanceof Collection<?> collection) {
      return collection.stream()
          .filter(String.class::isInstance)
          .map(String.class::cast)
          .filter(role -> !role.isBlank())
          .toList();
    }
    return List.of();
  }
}
