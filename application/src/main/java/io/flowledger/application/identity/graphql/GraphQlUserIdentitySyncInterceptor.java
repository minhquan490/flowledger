package io.flowledger.application.identity.graphql;

import io.flowledger.application.identity.event.UserIdentitySyncEvent;
import io.flowledger.application.identity.model.UserIdentityPayload;
import io.flowledger.core.auth.AppAuthProperties;
import io.flowledger.core.auth.AppAuthProperties.AuthMode;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Publishes a user identity synchronization event for GraphQL requests.
 */
@Component
@RequiredArgsConstructor
@Deprecated(forRemoval = true) // Migrate to use standard spring security
public class GraphQlUserIdentitySyncInterceptor implements WebGraphQlInterceptor {

  private static final String CLAIM_SUBJECT = "sub";
  private static final String CLAIM_EMAIL = "email";
  private static final String CLAIM_USERNAME = "preferred_username";
  private static final String CLAIM_FULL_NAME = "name";

  private final ApplicationEventPublisher eventPublisher;
  private final AppAuthProperties authProperties;

  /**
   * Intercepts GraphQL requests to publish identity synchronization events.
   *
   * @param request the GraphQL request
   * @param chain the remaining interceptor chain
   * @return the GraphQL response
   */
  @Override
  @NonNull
  public Mono<WebGraphQlResponse> intercept(@NonNull WebGraphQlRequest request, @NonNull Chain chain) {
    if (isKeycloakModeEnabled()) {
      publishIdentitySyncEvent();
    }
    return chain.next(request);
  }

  /**
   * Determines whether Keycloak authentication mode is enabled.
   *
   * @return true when Keycloak is the active authentication mode
   */
  private boolean isKeycloakModeEnabled() {
    return authProperties.getMode() == AuthMode.KEYCLOAK;
  }

  /**
   * Publishes a synchronization event if a JWT identity payload is available.
   */
  private void publishIdentitySyncEvent() {
    resolveIdentityPayload().ifPresent(payload -> eventPublisher.publishEvent(new UserIdentitySyncEvent(payload)));
  }

  /**
   * Resolves the identity payload from the current authentication.
   *
   * @return the identity payload if available and valid
   */
  private Optional<UserIdentityPayload> resolveIdentityPayload() {
    return resolveJwtAuthentication()
        .flatMap(this::extractPayload);
  }

  /**
   * Retrieves the current JWT authentication token if present.
   *
   * @return the JWT authentication token wrapped in an optional
   */
  private Optional<JwtAuthenticationToken> resolveJwtAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof JwtAuthenticationToken token) {
      return Optional.of(token);
    }
    return Optional.empty();
  }

  /**
   * Extracts identity attributes from the JWT authentication token.
   *
   * @param authentication the JWT authentication token
   * @return the identity payload if required claims are present
   */
  private Optional<UserIdentityPayload> extractPayload(JwtAuthenticationToken authentication) {
    Jwt jwt = authentication.getToken();
    Optional<String> email = resolveClaim(jwt, CLAIM_EMAIL);
    return email.map(s -> UserIdentityPayload.builder()
        .subject(resolveClaim(jwt, CLAIM_SUBJECT))
        .email(s)
        .username(resolveClaim(jwt, CLAIM_USERNAME))
        .fullName(resolveClaim(jwt, CLAIM_FULL_NAME))
        .build());
  }

  /**
   * Resolves a string claim from the JWT.
   *
   * @param jwt the JWT token
   * @param claimName the claim name to read
   * @return the claim value if present
   */
  private Optional<String> resolveClaim(Jwt jwt, String claimName) {
    String value = jwt.getClaimAsString(claimName);
    if (value == null || value.isBlank()) {
      return Optional.empty();
    }
    return Optional.of(value);
  }
}
