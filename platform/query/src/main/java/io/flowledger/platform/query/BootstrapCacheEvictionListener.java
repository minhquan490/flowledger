package io.flowledger.platform.query;

import io.flowledger.core.event.CacheEvictionEvent;
import io.flowledger.core.boot.BootstrapCacheNames;
import io.flowledger.platform.query.mapping.MappingExpressionResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.stereotype.Component;

/**
 * Evicts bootstrap-only caches after the application startup phase completes.
 */
@Component
@RequiredArgsConstructor
public class BootstrapCacheEvictionListener implements ApplicationListener<ApplicationStartedEvent> {
  private final ApplicationEventPublisher eventPublisher;

  /**
   * Publishes cache eviction events for bootstrap-only caches.
   *
   * @param event the Spring application started event
   */
  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {
    publishEviction(BootstrapCacheNames.CLASSPATH_SCANNER_SCAN);
    publishEviction(MappingExpressionResolver.CACHE_NAME);
  }

  /**
   * Publishes a cache eviction event for the provided cache name.
   *
   * @param cacheName the target cache name
   */
  private void publishEviction(String cacheName) {
    CacheEvictionEvent.Source source = new CacheEvictionEvent.Source(cacheName);
    eventPublisher.publishEvent(new CacheEvictionEvent(source));
  }
}
