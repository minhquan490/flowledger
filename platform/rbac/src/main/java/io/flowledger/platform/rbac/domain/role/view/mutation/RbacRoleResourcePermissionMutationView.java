package io.flowledger.platform.rbac.domain.role.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.role.entity.RbacRoleResourcePermission;
import io.flowledger.platform.rbac.domain.role.validator.RbacRoleResourcePermissionMutationViewPayloadValidator;
import io.flowledger.platform.rbac.domain.role.valueobject.RbacAction;
import io.flowledger.platform.rbac.domain.role.view.RbacRoleResourcePermissionView;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * Mutation-capable view for RBAC role resource permissions.
 */
@EntityView(RbacRoleResourcePermission.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "rbacRoleResourcePermissionMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = RbacRoleResourcePermissionMutationViewPayloadValidator.class
)
public interface RbacRoleResourcePermissionMutationView extends RbacRoleResourcePermissionView {
  /**
   * Sets the role identifier.
   *
   * @param roleId the role id
   */
  void setRoleId(UUID roleId);

  /**
   * Sets the resource identifier.
   *
   * @param resourceId the resource id
   */
  void setResourceId(UUID resourceId);

  /**
   * Sets the action.
   *
   * @param action the action
   */
  void setAction(RbacAction action);

  /**
   * Sets whether the action is allowed.
   *
   * @param allowed true when allowed
   */
  void setAllowed(boolean allowed);

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
