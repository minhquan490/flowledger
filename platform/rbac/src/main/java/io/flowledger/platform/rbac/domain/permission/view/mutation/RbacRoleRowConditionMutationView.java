package io.flowledger.platform.rbac.domain.permission.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.permission.entity.RbacRoleRowCondition;
import io.flowledger.platform.rbac.domain.permission.validator.RbacRoleRowConditionMutationViewPayloadValidator;
import io.flowledger.platform.rbac.domain.permission.view.RbacRoleRowConditionView;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;

import java.time.Instant;
import java.util.UUID;

/**
 * Mutation-capable view for RBAC role row conditions.
 */
@EntityView(RbacRoleRowCondition.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "rbacRoleRowConditionMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = RbacRoleRowConditionMutationViewPayloadValidator.class
)
public interface RbacRoleRowConditionMutationView extends RbacRoleRowConditionView {
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
