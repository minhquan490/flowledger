package io.flowledger.domain.identity.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.identity.entity.UserDevice;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for user devices.
 */
@EntityView(UserDevice.class)
@GraphQlModel(value = "userDevice", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface UserDeviceView {

  /**
   * Returns the device identifier.
   *
   * @return the device id
   */
  UUID getId();

  /**
   * Returns the linked user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the device name.
   *
   * @return the device name
   */
  String getDeviceName();

  /**
   * Returns the device type.
   *
   * @return the device type
   */
  String getDeviceType();

  /**
   * Returns the last seen timestamp.
   *
   * @return the last seen timestamp
   */
  Instant getLastSeenAt();

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
