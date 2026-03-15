package io.flowledger.application.identity.listener;

import io.flowledger.application.identity.event.UserIdentitySyncEvent;
import io.flowledger.application.identity.service.UserIdentitySyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Handles identity synchronization events asynchronously.
 */
@Component
@RequiredArgsConstructor
public class UserIdentitySyncEventHandler {

  private final UserIdentitySyncService userIdentitySyncService;

  /**
   * Handles a user identity synchronization event.
   *
   * @param event the identity synchronization event
   */
  @Async("identitySyncExecutor")
  @EventListener
  public void handleUserIdentitySyncEvent(UserIdentitySyncEvent event) {
    userIdentitySyncService.syncUser(event.getPayload());
  }
}
