package io.flowledger.platform.rbac.domain.role.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import io.flowledger.platform.rbac.domain.role.valueobject.RbacAction;
import io.flowledger.platform.rbac.domain.role.entity.RbacRoleResourcePermission;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for RBAC role resource permissions.
 */
@EntityView(RbacRoleResourcePermission.class)
@GraphQlModel(value = "rbacRoleResourcePermission", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface RbacRoleResourcePermissionView {

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
