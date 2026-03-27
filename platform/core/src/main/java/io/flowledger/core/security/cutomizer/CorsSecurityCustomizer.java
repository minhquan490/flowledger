package io.flowledger.core.security.cutomizer;

import io.flowledger.core.security.SecurityCustomizer;
import io.flowledger.core.security.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configures CORS for HTTP security.
 */
@RequiredArgsConstructor
public class CorsSecurityCustomizer implements SecurityCustomizer {

  private final SecurityProperties authProperties;

  /**
   * Applies CORS configuration based on application properties.
   *
   * @param httpSecurity the HTTP security builder
   */
  @Override
  public void customize(HttpSecurity httpSecurity) {
    SecurityProperties.CorsProperties corsProperties = authProperties.getCors();
    if (!corsProperties.isEnabled()) {
      httpSecurity.cors(AbstractHttpConfigurer::disable);
      return;
    }
    httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource(corsProperties)));
  }

  /**
   * Builds a CORS configuration source using the provided properties.
   *
   * @param corsProperties the CORS configuration properties
   * @return the CORS configuration source
   */
  private CorsConfigurationSource corsConfigurationSource(SecurityProperties.CorsProperties corsProperties) {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(corsProperties.getAllowedOrigins());
    configuration.setAllowedMethods(corsProperties.getAllowedMethods());
    configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
    configuration.setAllowCredentials(corsProperties.isAllowCredentials());
    configuration.setMaxAge(corsProperties.getMaxAge());

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
