package io.flowledger.core.security.cutomizer;

import io.flowledger.core.security.SecurityCustomizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Configures stateless session management.
 */
public class SessionSecurityCustomizer implements SecurityCustomizer {

  /**
   * Applies session management configuration.
   *
   * @param httpSecurity the HTTP security builder
   */
  @Override
  public void customize(HttpSecurity httpSecurity) {
    httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
  }
}
