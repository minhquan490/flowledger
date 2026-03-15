package io.flowledger.platform.rbac.domain.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.RbacAction;
import io.flowledger.platform.rbac.domain.entity.RbacRoleResourcePermissionEntity;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for RBAC role resource permissions.
 */
@EntityView(RbacRoleResourcePermissionEntity.class)
@GraphQlModel("rbacRoleResourcePermission")
public interface RbacRoleResourcePermissionView {

  /**
   * Returns the permission identifier.
   *
   * @return the permission id
   */
  UUID getId();

  /**
   * Returns the role identifier.
   *
   * @return the role id
   */
  UUID getRoleId();

  /**
   * Returns the resource identifier.
   *
   * @return the resource id
   */
  UUID getResourceId();

  /**
   * Returns the action.
   *
   * @return the action
   */
  RbacAction getAction();

  /**
   * Returns whether the action is allowed.
   *
   * @return true when allowed
   */
  boolean isAllowed();

  /**
   * Returns whether the permission is system-managed.
   *
   * @return true when system-managed
   */
  boolean isSystemManaged();

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
