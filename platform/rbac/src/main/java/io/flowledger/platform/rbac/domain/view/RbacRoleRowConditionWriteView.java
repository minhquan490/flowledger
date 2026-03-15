package io.flowledger.platform.rbac.domain.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.entity.RbacRoleRowConditionEntity;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for RBAC role row conditions.
 */
@EntityView(RbacRoleRowConditionEntity.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("rbacRoleRowConditionWrite")
public interface RbacRoleRowConditionWriteView extends RbacRoleRowConditionView {

  /**
   * Sets the condition identifier.
   *
   * @param id the condition id
   */
  void setId(UUID id);

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
   * Sets the condition JSON.
   *
   * @param conditionJson the condition JSON
   */
  void setConditionJson(String conditionJson);

  /**
   * Sets whether the condition is system-managed.
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
