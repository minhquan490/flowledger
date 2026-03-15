package io.flowledger.domain.identity.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.identity.entity.UserSession;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for user sessions.
 */
@EntityView(UserSession.class)
@GraphQlModel(value = "userSession", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface UserSessionView {

  /**
   * Returns the session identifier.
   *
   * @return the session id
   */
  UUID getId();

  /**
   * Returns the linked user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the session status.
   *
   * @return the session status
   */
  String getStatus();

  /**
   * Returns the device identifier.
   *
   * @return the device id
   */
  UUID getDeviceId();

  /**
   * Returns the session expiration timestamp.
   *
   * @return the expiration timestamp
   */
  Instant getExpiresAt();

  /**
   * Returns the creation timestamp.
   *
   * @return the creation timestamp
   */
  Instant getCreatedAt();

  /**
   * Returns the update timestamp.
   *
   * @return the update timestamp
   */
  Instant getUpdatedAt();
}
