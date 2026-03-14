package io.flowledger.core.event;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Handles {@link CacheEvictionEvent} and clears the targeted cache.
 */
@Component
@RequiredArgsConstructor
public class CacheEvictionEventHandler implements ApplicationListener<CacheEvictionEvent> {
  private final CacheManager cacheManager;

  /**
   * Clears the cache referenced by the incoming event if it exists.
   *
   * @param event the cache eviction event containing the target cache name
   */
  @Override
  public void onApplicationEvent(CacheEvictionEvent event) {
    CacheEvictionEvent.Source source = event.getSource();

    Cache cache = cacheManager.getCache(source.cacheName());
    if (cache != null) {
      cache.clear();
    }
  }
}
