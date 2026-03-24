package io.flowledger.platform.rbac.domain.permission.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.permission.entity.RbacRoleFieldActionPermission;
import io.flowledger.platform.rbac.domain.permission.validator.RbacRoleFieldActionPermissionMutationViewPayloadValidator;
import io.flowledger.platform.rbac.domain.permission.view.RbacRoleFieldActionPermissionView;
import io.flowledger.platform.rbac.domain.role.valueobject.RbacFieldAction;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * Mutation-capable view for RBAC role field action permissions.
 */
@EntityView(RbacRoleFieldActionPermission.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "rbacRoleFieldActionPermissionMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = RbacRoleFieldActionPermissionMutationViewPayloadValidator.class
)
public interface RbacRoleFieldActionPermissionMutationView extends RbacRoleFieldActionPermissionView {

  /**
   * Sets the role identifier.
   *
   * @param roleId the role id
   */
  void setRoleId(UUID roleId);

  /**
   * Sets the resource field identifier.
   *
   * @param resourceFieldId the resource field id
   */
  void setResourceFieldId(UUID resourceFieldId);

  /**
   * Sets the action.
   *
   * @param action the action
   */
  void setAction(RbacFieldAction action);

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
