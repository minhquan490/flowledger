package io.flowledger.core.security.cutomizer;

import io.flowledger.core.security.SecurityAccessDeniedHandler;
import io.flowledger.core.security.SecurityAuthenticationEntryPoint;
import io.flowledger.core.security.SecurityCustomizer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Configures exception handling for authentication and access denied responses.
 */
@RequiredArgsConstructor
public class ExceptionHandlingSecurityCustomizer implements SecurityCustomizer {

  private final SecurityAuthenticationEntryPoint authenticationEntryPoint;
  private final SecurityAccessDeniedHandler accessDeniedHandler;

  /**
   * Applies exception handling configuration.
   *
   * @param httpSecurity the HTTP security builder
   */
  @Override
  public void customize(HttpSecurity httpSecurity) {
    httpSecurity.exceptionHandling(exceptionHandling -> exceptionHandling
        .authenticationEntryPoint(authenticationEntryPoint)
        .accessDeniedHandler(accessDeniedHandler));
  }
}
