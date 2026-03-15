package io.flowledger.application.identity.config;

import java.util.concurrent.Executor;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Enables asynchronous event processing for identity synchronization.
 */
@Configuration
@EnableAsync
@NoArgsConstructor
public class AsyncEventConfiguration {

  private static final int CORE_POOL_SIZE = 4;
  private static final int MAX_POOL_SIZE = 8;
  private static final int QUEUE_CAPACITY = 1000;
  private static final String THREAD_NAME_PREFIX = "identity-sync-";

  /**
   * Provides a dedicated executor for identity synchronization events.
   *
   * @return the executor used for async event processing
   */
  @Bean(name = "identitySyncExecutor")
  public Executor identitySyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(CORE_POOL_SIZE);
    executor.setMaxPoolSize(MAX_POOL_SIZE);
    executor.setQueueCapacity(QUEUE_CAPACITY);
    executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
    executor.initialize();
    return executor;
  }
}
