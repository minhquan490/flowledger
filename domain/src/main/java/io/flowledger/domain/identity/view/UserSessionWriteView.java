package io.flowledger.domain.identity.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.identity.entity.UserSession;
import io.flowledger.platform.graphql.domain.GraphQlModel;

import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for user sessions.
 */
@EntityView(UserSession.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("userSessionWrite")
public interface UserSessionWriteView extends UserSessionView {

  /**
   * Sets the session identifier.
   *
   * @param id the session id
   */
  void setId(UUID id);

  /**
   * Sets the user identifier.
   *
   * @param userId the user id
   */
  void setUserId(UUID userId);

  /**
   * Sets the device identifier.
   *
   * @param deviceId the device id
   */
  void setDeviceId(UUID deviceId);

  /**
   * Sets the session status.
   *
   * @param status the status
   */
  void setStatus(String status);

  /**
   * Sets the expiration timestamp.
   *
   * @param expiresAt the expiration timestamp
   */
  void setExpiresAt(Instant expiresAt);

  /**
   * Sets the creation timestamp.
   *
   * @param createdAt the creation timestamp
   */
  void setCreatedAt(Instant createdAt);

  /**
   * Sets the update timestamp.
   *
   * @param updatedAt the update timestamp
   */
  void setUpdatedAt(Instant updatedAt);
}
