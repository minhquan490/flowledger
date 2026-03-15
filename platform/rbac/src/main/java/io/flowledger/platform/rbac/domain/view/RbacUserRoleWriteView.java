package io.flowledger.platform.rbac.domain.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.entity.RbacUserRoleEntity;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for RBAC user-role assignments.
 */
@EntityView(RbacUserRoleEntity.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("rbacUserRoleWrite")
public interface RbacUserRoleWriteView extends RbacUserRoleView {

  /**
   * Sets the assignment identifier.
   *
   * @param id the assignment id
   */
  void setId(UUID id);

  /**
   * Sets the user identifier.
   *
   * @param userId the user id
   */
  void setUserId(UUID userId);

  /**
   * Sets the role identifier.
   *
   * @param roleId the role id
   */
  void setRoleId(UUID roleId);

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
