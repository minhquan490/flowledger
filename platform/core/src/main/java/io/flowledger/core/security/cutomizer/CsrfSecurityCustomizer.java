package io.flowledger.core.security.cutomizer;

import io.flowledger.core.security.SecurityCustomizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * Disables CSRF protection for stateless APIs.
 */
public class CsrfSecurityCustomizer implements SecurityCustomizer {

  /**
   * Applies CSRF configuration.
   *
   * @param httpSecurity the HTTP security builder
   */
  @Override
  public void customize(HttpSecurity httpSecurity) {
    httpSecurity.csrf(AbstractHttpConfigurer::disable);
  }
}
