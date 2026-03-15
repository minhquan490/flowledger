package io.flowledger.platform.rbac.domain.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.entity.RbacRoleFieldPermissionEntity;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for RBAC role field permissions.
 */
@EntityView(RbacRoleFieldPermissionEntity.class)
@GraphQlModel("rbacRoleFieldPermission")
public interface RbacRoleFieldPermissionView {

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
