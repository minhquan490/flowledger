package io.flowledger.domain.identity.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.identity.entity.UserSession;
import io.flowledger.domain.identity.validator.UserSessionWriteViewPayloadValidator;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;

import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for user sessions.
 */
@EntityView(UserSession.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "userSessionWrite",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = UserSessionWriteViewPayloadValidator.class
)
public interface UserSessionWriteView extends UserSessionView {
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
