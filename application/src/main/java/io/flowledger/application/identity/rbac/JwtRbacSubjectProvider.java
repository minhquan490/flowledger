package io.flowledger.application.identity.rbac;

import io.flowledger.application.identity.service.UserIdentityLookupService;
import io.flowledger.core.security.UnAuthenticationException;
import io.flowledger.platform.rbac.application.RbacSubjectProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Resolves the RBAC subject identifier from the current JWT authentication.
 */
@Component
@RequiredArgsConstructor
public class JwtRbacSubjectProvider implements RbacSubjectProvider {

  private static final String CLAIM_EMAIL = "email";

  private final UserIdentityLookupService lookupService;

  /**
   * Resolves the current subject identifier from the JWT claims.
   *
   * @return the current subject identifier if available
   * @throws UnAuthenticationException when the subject identifier cannot be resolved
   */
  @Override
  public Optional<UUID> currentSubjectId() {
    Optional<UUID> subjectId = resolveJwtAuthentication()
        .flatMap(this::resolveSubjectId);
    if (subjectId.isEmpty()) {
      throw new UnAuthenticationException("Unable to resolve RBAC subject from JWT.");
    }
    return subjectId;
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
   * Resolves the subject identifier from JWT claims.
   *
   * @param authentication the JWT authentication token
   * @return the subject identifier if resolved
   */
  private Optional<UUID> resolveSubjectId(JwtAuthenticationToken authentication) {
    Jwt jwt = authentication.getToken();
    return resolveEmail(jwt)
        .flatMap(lookupService::findUserIdByEmail);
  }

  /**
   * Resolves the email claim from the JWT.
   *
   * @param jwt the JWT token
   * @return the email claim if present
   */
  private Optional<String> resolveEmail(Jwt jwt) {
    return resolveClaim(jwt);
  }

  /**
   * Resolves a string claim from the JWT.
   *
   * @param jwt the JWT token
   * @return the claim value if present
   */
  private Optional<String> resolveClaim(Jwt jwt) {
    String value = jwt.getClaimAsString(JwtRbacSubjectProvider.CLAIM_EMAIL);
    if (value == null || value.isBlank()) {
      return Optional.empty();
    }
    return Optional.of(value);
  }
}
