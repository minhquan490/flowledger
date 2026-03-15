package io.flowledger.platform.rbac.domain.role.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.role.validator.RbacRoleFieldPermissionWriteViewPayloadValidator;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import io.flowledger.platform.rbac.domain.role.entity.RbacRoleFieldPermission;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for RBAC role field permissions.
 */
@EntityView(RbacRoleFieldPermission.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "rbacRoleFieldPermissionWrite",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = RbacRoleFieldPermissionWriteViewPayloadValidator.class
)
public interface RbacRoleFieldPermissionWriteView extends RbacRoleFieldPermissionView {
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
   * Sets the field name.
   *
   * @param fieldName the field name
   */
  void setFieldName(String fieldName);

  /**
   * Sets whether the field is readable.
   *
   * @param canRead true when readable
   */
  void setCanRead(boolean canRead);

  /**
   * Sets whether the field is writable.
   *
   * @param canWrite true when writable
   */
  void setCanWrite(boolean canWrite);

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
