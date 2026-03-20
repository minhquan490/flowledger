package io.flowledger.platform.rbac.domain.role.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.role.aggregate.RbacRole;
import io.flowledger.platform.rbac.domain.role.validator.RbacRoleMutationViewPayloadValidator;
import io.flowledger.platform.rbac.domain.role.view.RbacRoleView;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;

/**
 * Mutation-capable view for RBAC roles.
 */
@EntityView(RbacRole.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "rbacRoleMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = RbacRoleMutationViewPayloadValidator.class
)
public interface RbacRoleMutationView extends RbacRoleView {
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
