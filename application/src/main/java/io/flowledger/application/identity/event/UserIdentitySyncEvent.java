package io.flowledger.application.identity.event;

import io.flowledger.application.identity.model.UserIdentityPayload;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * Application event that triggers synchronization of user identity data.
 */
@ToString
public class UserIdentitySyncEvent extends ApplicationEvent {

  /**
   * Creates a new identity synchronization event.
   *
   * @param payload the identity payload extracted from the JWT
   */
  public UserIdentitySyncEvent(UserIdentityPayload payload) {
    super(payload);
  }

  /**
   * Returns the identity payload for synchronization.
   *
   * @return the identity payload extracted from the JWT
   */
  public UserIdentityPayload getPayload() {
    return (UserIdentityPayload) getSource();
  }
}
