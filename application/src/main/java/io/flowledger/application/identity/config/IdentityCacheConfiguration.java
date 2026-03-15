package io.flowledger.application.identity.config;

import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Enables caching for identity lookups.
 */
@Configuration
@EnableCaching
@NoArgsConstructor
public class IdentityCacheConfiguration {
}
