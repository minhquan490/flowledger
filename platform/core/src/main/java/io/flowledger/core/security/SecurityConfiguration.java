package io.flowledger.core.security;

import io.flowledger.core.security.cutomizer.AuthorizationSecurityCustomizer;
import io.flowledger.core.security.cutomizer.CorsSecurityCustomizer;
import io.flowledger.core.security.cutomizer.CsrfSecurityCustomizer;
import io.flowledger.core.security.cutomizer.ExceptionHandlingSecurityCustomizer;
import io.flowledger.core.security.cutomizer.KeycloakResourceServerSecurityCustomizer;
import io.flowledger.core.security.cutomizer.SessionSecurityCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Spring Security configuration for authentication.
 */
@Configuration
public class SecurityConfiguration {

  /**
   * Configures the security filter chain for JWT-based Keycloak authentication.
   *
   * @param httpSecurity the HTTP security builder
   * @return the configured security filter chain
   */
  @Bean
  public SecurityFilterChain keycloakSecurityFilterChain(
      HttpSecurity httpSecurity,
      SecurityProperties securityProperties,
      List<SecurityCustomizer> customizers
  ) {
    SecurityFilterChainBuilder builder = new SecurityFilterChainBuilder(httpSecurity, securityProperties);
    return builder.build(security -> applyCustomizers(security, customizers));
  }

  /**
   * Creates the CORS security customizer.
   *
   * @return the security customizer
   */
  @Bean
  @Order(SecurityCustomizerOrder.CORS)
  public SecurityCustomizer corsSecurityCustomizer(SecurityProperties securityProperties) {
    return new CorsSecurityCustomizer(securityProperties);
  }

  /**
   * Creates the CSRF security customizer.
   *
   * @return the security customizer
   */
  @Bean
  @Order(SecurityCustomizerOrder.CSRF)
  public SecurityCustomizer csrfSecurityCustomizer() {
    return new CsrfSecurityCustomizer();
  }

  /**
   * Creates the session management security customizer.
   *
   * @return the security customizer
   */
  @Bean
  @Order(SecurityCustomizerOrder.SESSION)
  public SecurityCustomizer sessionSecurityCustomizer() {
    return new SessionSecurityCustomizer();
  }

  /**
   * Creates the authorization security customizer.
   *
   * @return the security customizer
   */
  @Bean
  @Order(SecurityCustomizerOrder.AUTHORIZATION)
  public SecurityCustomizer authorizationSecurityCustomizer() {
    return new AuthorizationSecurityCustomizer();
  }

  /**
   * Creates the OAuth2 resource server security customizer.
   *
   * @return the security customizer
   */
  @Bean
  @Order(SecurityCustomizerOrder.RESOURCE_SERVER)
  @ConditionalOnProperty(
      prefix = SecurityProperties.PREFIX,
      name = "mode",
      havingValue = KeycloakResourceServerSecurityCustomizer.KEYCLOAK_MODE
  )
  public SecurityCustomizer oauth2ResourceServerSecurityCustomizer(SecurityProperties securityProperties) {
    return new KeycloakResourceServerSecurityCustomizer(securityProperties);
  }

  /**
   * Creates the exception handling security customizer.
   *
   * @return the security customizer
   */
  @Bean
  @Order(SecurityCustomizerOrder.EXCEPTION_HANDLING)
  public SecurityCustomizer exceptionHandlingSecurityCustomizer(ObjectMapper objectMapper) {
    return new ExceptionHandlingSecurityCustomizer(
        new SecurityAuthenticationEntryPoint(objectMapper),
        new SecurityAccessDeniedHandler(objectMapper)
    );
  }

  /**
   * Applies all registered security customizers.
   *
   * @param httpSecurity the HTTP security builder
   * @param customizers the customizers to apply
   */
  private void applyCustomizers(HttpSecurity httpSecurity, List<SecurityCustomizer> customizers) {
    customizers.forEach(customizer -> applyCustomizer(customizer, httpSecurity));
  }

  /**
   * Applies a single customizer and wraps checked exceptions.
   *
   * @param customizer the customizer to apply
   * @param httpSecurity the HTTP security builder
   */
  private void applyCustomizer(SecurityCustomizer customizer, HttpSecurity httpSecurity) {
    try {
      customizer.customize(httpSecurity);
    } catch (Exception ex) {
      throw new IllegalStateException("Failed to apply security customization.", ex);
    }
  }
}
