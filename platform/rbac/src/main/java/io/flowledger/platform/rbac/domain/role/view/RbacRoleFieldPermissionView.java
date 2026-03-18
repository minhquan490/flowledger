package io.flowledger.platform.rbac.domain.role.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import io.flowledger.platform.rbac.domain.role.entity.RbacRoleFieldPermission;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for RBAC role field permissions.
 */
@EntityView(RbacRoleFieldPermission.class)
@GraphQlModel(value = "rbacRoleFieldPermission", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface RbacRoleFieldPermissionView {

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
   * Returns the field name.
   *
   * @return the field name
   */
  String getFieldName();

  /**
   * Returns whether the field is readable.
   *
   * @return true when readable
   */
  boolean isCanRead();

  /**
   * Returns whether the field is writable.
   *
   * @return true when writable
   */
  boolean isCanWrite();

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
