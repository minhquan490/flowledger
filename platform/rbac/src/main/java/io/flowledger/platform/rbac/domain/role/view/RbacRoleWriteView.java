package io.flowledger.platform.rbac.domain.role.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.role.validator.RbacRoleWriteViewPayloadValidator;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import io.flowledger.platform.rbac.domain.role.aggregate.RbacRole;
import java.time.Instant;

/**
 * Write-capable view for RBAC roles.
 */
@EntityView(RbacRole.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "rbacRoleWrite",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = RbacRoleWriteViewPayloadValidator.class
)
public interface RbacRoleWriteView extends RbacRoleView {
  /**
   * Sets the role code.
   *
   * @param code the role code
   */
  void setCode(String code);

  /**
   * Sets the role name.
   *
   * @param name the role name
   */
  void setName(String name);

  /**
   * Sets whether the role is the default role.
   *
   * @param defaultRole true when default
   */
  void setDefaultRole(boolean defaultRole);

  /**
   * Sets whether the role is system-managed.
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
