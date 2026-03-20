package io.flowledger.domain.identity.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.identity.entity.UserDevice;
import io.flowledger.domain.identity.validator.UserDeviceMutationViewPayloadValidator;
import io.flowledger.domain.identity.view.UserDeviceView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * Mutation-capable view for user devices.
 */
@EntityView(UserDevice.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "userDeviceMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = UserDeviceMutationViewPayloadValidator.class
)
public interface UserDeviceMutationView extends UserDeviceView {
  /**
   * Sets the user identifier.
   *
   * @param userId the user id
   */
  void setUserId(UUID userId);

  /**
   * Sets the device name.
   *
   * @param deviceName the device name
   */
  void setDeviceName(String deviceName);

  /**
   * Sets the device type.
   *
   * @param deviceType the device type
   */
  void setDeviceType(String deviceType);

  /**
   * Sets the last seen timestamp.
   *
   * @param lastSeenAt the last seen timestamp
   */
  void setLastSeenAt(Instant lastSeenAt);

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
