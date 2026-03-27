package io.flowledger.core.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Builder for assembling a {@link SecurityFilterChain} with optional custom configuration.
 */
@RequiredArgsConstructor
public class SecurityFilterChainBuilder {

  private final HttpSecurity httpSecurity;
  private final SecurityProperties securityProperties;

  /**
   * Builds a security filter chain and applies optional customizations.
   *
   * @param httpSecurityCustomizer optional customizer applied before the chain is built
   * @return the configured security filter chain
   * @throws IllegalStateException when required configuration is missing
   */
  public SecurityFilterChain build(Consumer<HttpSecurity> httpSecurityCustomizer) {
    validate();
    if (httpSecurityCustomizer != null) {
      httpSecurityCustomizer.accept(httpSecurity);
    }
    return httpSecurity.build();
  }

  /**
   * Validates the builder state before building the security filter chain.
   *
   * @throws IllegalStateException when required configuration is missing
   */
  private void validate() {
    if (Objects.isNull(securityProperties)) {
      throw new IllegalStateException("SecurityProperties must be provided.");
    }
  }
}
