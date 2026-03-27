package io.flowledger.application.config;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application-level Jackson configuration to ensure an {@link ObjectMapper} is available.
 */
@Configuration
public class JacksonConfiguration {

  /**
   * Builds the shared {@link ObjectMapper} for the application context.
   *
   * @return the configured object mapper
   */
  @Bean
  public ObjectMapper objectMapper() {
    return JsonMapper.builder()
        .findAndAddModules()
        .build();
  }
}
