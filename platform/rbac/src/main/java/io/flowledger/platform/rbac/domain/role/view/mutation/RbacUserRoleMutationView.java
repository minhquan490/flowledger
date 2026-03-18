package io.flowledger.platform.rbac.domain.role.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.role.entity.RbacUserRole;
import io.flowledger.platform.rbac.domain.role.validator.RbacUserRoleMutationViewPayloadValidator;
import io.flowledger.platform.rbac.domain.role.view.RbacUserRoleView;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * Mutation-capable view for RBAC user-role assignments.
 */
@EntityView(RbacUserRole.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "rbacUserRoleWrite",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = RbacUserRoleMutationViewPayloadValidator.class
)
public interface RbacUserRoleMutationView extends RbacUserRoleView {
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
