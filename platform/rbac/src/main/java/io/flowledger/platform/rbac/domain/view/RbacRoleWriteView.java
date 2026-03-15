package io.flowledger.platform.rbac.domain.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.entity.RbacRoleEntity;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for RBAC roles.
 */
@EntityView(RbacRoleEntity.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("rbacRoleWrite")
public interface RbacRoleWriteView extends RbacRoleView {

  /**
   * Sets the role identifier.
   *
   * @param id the role id
   */
  void setId(UUID id);

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
