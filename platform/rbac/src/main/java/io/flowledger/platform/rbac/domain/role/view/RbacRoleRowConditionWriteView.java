package io.flowledger.platform.rbac.domain.role.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.role.validator.RbacRoleRowConditionWriteViewPayloadValidator;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import io.flowledger.platform.rbac.domain.role.entity.RbacRoleRowCondition;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for RBAC role row conditions.
 */
@EntityView(RbacRoleRowCondition.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "rbacRoleRowConditionWrite",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = RbacRoleRowConditionWriteViewPayloadValidator.class
)
public interface RbacRoleRowConditionWriteView extends RbacRoleRowConditionView {
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
