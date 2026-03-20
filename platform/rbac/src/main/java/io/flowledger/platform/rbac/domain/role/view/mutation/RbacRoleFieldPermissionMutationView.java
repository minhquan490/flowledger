package io.flowledger.platform.rbac.domain.role.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.role.entity.RbacRoleFieldPermission;
import io.flowledger.platform.rbac.domain.role.validator.RbacRoleFieldPermissionMutationViewPayloadValidator;
import io.flowledger.platform.rbac.domain.role.view.RbacRoleFieldPermissionView;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * Mutation-capable view for RBAC role field permissions.
 */
@EntityView(RbacRoleFieldPermission.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "rbacRoleFieldPermissionMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = RbacRoleFieldPermissionMutationViewPayloadValidator.class
)
public interface RbacRoleFieldPermissionMutationView extends RbacRoleFieldPermissionView {
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
