package io.flowledger.platform.rbac.domain.permission.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.permission.entity.RbacRoleFieldActionPermission;
import io.flowledger.platform.rbac.domain.role.valueobject.RbacFieldAction;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for RBAC role field action permissions.
 */
@EntityView(RbacRoleFieldActionPermission.class)
@GraphQlModel(value = "rbacRoleFieldActionPermission", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface RbacRoleFieldActionPermissionView {

  /**
   * Returns the permission identifier.
   *
   * @return the permission id
   */
  @IdMapping
  UUID getId();

  /**
   * Returns the role identifier.
   *
   * @return the role id
   */
  UUID getRoleId();

  /**
   * Returns the resource field identifier.
   *
   * @return the resource field id
   */
  UUID getResourceFieldId();

  /**
   * Returns the action.
   *
   * @return the action
   */
  RbacFieldAction getAction();

  /**
   * Returns whether the action is allowed.
   *
   * @return true when allowed
   */
  boolean isAllowed();

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
