package io.flowledger.core.event;

import org.springframework.context.ApplicationEvent;

/**
 * Application event used to trigger cache eviction for a specific cache name.
 */
public class CacheEvictionEvent extends ApplicationEvent {
  /**
   * Creates a new cache eviction event.
   *
   * @param source event payload containing cache eviction metadata
   */
  public CacheEvictionEvent(Source source) {
    super(source);
  }

  /**
   * Returns the typed event source payload.
   *
   * @return event source containing the target cache name
   */
  @Override
  public Source getSource() {
    return (Source) super.getSource();
  }

  /**
   * Event payload containing the cache name to evict.
   *
   * @param cacheName the cache name resolved by {@code CacheManager}
   */
  public record Source(String cacheName) {}
}
