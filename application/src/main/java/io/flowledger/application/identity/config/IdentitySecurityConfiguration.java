package io.flowledger.application.identity.config;

import io.flowledger.application.identity.security.UserIdentitySyncFilter;
import io.flowledger.core.security.SecurityCustomizer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;

/**
 * Configuration for identity-related security components.
 */
@Configuration
@RequiredArgsConstructor
public class IdentitySecurityConfiguration {

  private final ApplicationEventPublisher eventPublisher;

  /**
   * Creates the identity synchronization filter.
   *
   * @return the identity sync filter
   */
  @Bean
  public UserIdentitySyncFilter userIdentitySyncFilter() {
    return new UserIdentitySyncFilter(eventPublisher);
  }

  /**
   * Registers the identity sync filter in the security filter chain.
   *
   * @param filter the identity sync filter
   * @return the security customizer
   */
  @Bean
  @Order(70)
  public SecurityCustomizer userIdentitySyncSecurityCustomizer(UserIdentitySyncFilter filter) {
    return httpSecurity -> addIdentitySyncFilter(httpSecurity, filter);
  }

  /**
   * Adds the identity sync filter after bearer token authentication.
   *
   * @param httpSecurity the HTTP security builder
   * @param filter the identity sync filter
   */
  private void addIdentitySyncFilter(HttpSecurity httpSecurity, UserIdentitySyncFilter filter) {
    httpSecurity.addFilterAfter(filter, BearerTokenAuthenticationFilter.class);
  }
}
