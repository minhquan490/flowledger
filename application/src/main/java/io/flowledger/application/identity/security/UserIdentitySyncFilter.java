package io.flowledger.application.identity.security;

import io.flowledger.application.identity.event.UserIdentitySyncEvent;
import io.flowledger.application.identity.model.UserIdentityPayload;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Filter that publishes user identity sync events when a JWT is present.
 */
@RequiredArgsConstructor
public class UserIdentitySyncFilter extends OncePerRequestFilter {

  private static final String CLAIM_EMAIL = "email";
  private static final String CLAIM_USERNAME = "preferred_username";
  private static final String CLAIM_FULL_NAME = "name";
  private static final String CLAIM_FIRST_NAME = "given_name";
  private static final String CLAIM_LAST_NAME = "family_name";

  private final ApplicationEventPublisher eventPublisher;

  /**
   * Publishes an identity sync event when a valid JWT is available.
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @param filterChain the filter chain
   * @throws ServletException when the filter chain fails
   * @throws IOException when the request cannot be read
   */
  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    resolveJwtAuthentication()
        .map(this::buildPayload)
        .filter(payload -> StringUtils.hasText(payload.getEmail()))
        .ifPresent(payload -> eventPublisher.publishEvent(new UserIdentitySyncEvent(payload)));
    filterChain.doFilter(request, response);
  }

  /**
   * Resolves the current JWT authentication token if present.
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
   * Builds a user identity payload from the JWT.
   *
   * @param authentication the JWT authentication token
   * @return the payload to synchronize
   */
  private UserIdentityPayload buildPayload(JwtAuthenticationToken authentication) {
    Jwt jwt = authentication.getToken();
    return UserIdentityPayload.builder()
        .email(Optional.ofNullable(jwt.getClaimAsString(CLAIM_EMAIL)).orElse(""))
        .username(resolveClaim(jwt, CLAIM_USERNAME))
        .fullName(resolveClaim(jwt, CLAIM_FULL_NAME))
        .firstName(resolveClaim(jwt, CLAIM_FIRST_NAME))
        .lastName(resolveClaim(jwt, CLAIM_LAST_NAME))
        .build();
  }

  /**
   * Resolves a string claim from the JWT.
   *
   * @param jwt the JWT token
   * @param claimName the claim name
   * @return the claim value if present
   */
  private Optional<String> resolveClaim(Jwt jwt, String claimName) {
    String value = jwt.getClaimAsString(claimName);
    if (!StringUtils.hasText(value)) {
      return Optional.empty();
    }
    return Optional.of(value);
  }
}
