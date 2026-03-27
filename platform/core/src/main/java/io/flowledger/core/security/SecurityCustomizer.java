package io.flowledger.core.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Customizes {@link HttpSecurity} configuration prior to building the security filter chain.
 */
@FunctionalInterface
public interface SecurityCustomizer {

  /**
   * Applies custom configuration to the provided {@link HttpSecurity}.
   *
   * @param httpSecurity the HTTP security builder
   */
  void customize(HttpSecurity httpSecurity);
}
