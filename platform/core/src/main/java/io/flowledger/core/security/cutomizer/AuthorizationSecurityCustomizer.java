package io.flowledger.core.security.cutomizer;

import io.flowledger.core.security.SecurityCustomizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Configures authorization rules for HTTP requests.
 */
public class AuthorizationSecurityCustomizer implements SecurityCustomizer {

  /**
   * Applies authorization rules to require authentication for all requests.
   *
   * @param httpSecurity the HTTP security builder
   */
  @Override
  public void customize(HttpSecurity httpSecurity) {
    httpSecurity.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
  }
}
