package io.flowledger.platform.rbac.domain.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.RbacAction;
import io.flowledger.platform.rbac.domain.entity.RbacRoleResourcePermissionEntity;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for RBAC role resource permissions.
 */
@EntityView(RbacRoleResourcePermissionEntity.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("rbacRoleResourcePermissionWrite")
public interface RbacRoleResourcePermissionWriteView extends RbacRoleResourcePermissionView {

  /**
   * Sets the permission identifier.
   *
   * @param id the permission id
   */
  void setId(UUID id);

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
   * Sets whether the permission is system-managed.
   *
   * @param systemManaged true when system-managed
   */
  void setSystemManaged(boolean systemManaged);

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
