package io.flowledger.platform.rbac.domain.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.entity.RbacUserRoleEntity;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for RBAC user-role assignments.
 */
@EntityView(RbacUserRoleEntity.class)
@GraphQlModel("rbacUserRole")
public interface RbacUserRoleView {

  /**
   * Returns the assignment identifier.
   *
   * @return the assignment id
   */
  UUID getId();

  /**
   * Returns the user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the role identifier.
   *
   * @return the role id
   */
  UUID getRoleId();

  /**
   * Returns the creation timestamp.
   *
   * @return the creation timestamp
   */
  Instant getCreatedAt();

  /**
   * Returns the update timestamp.
   *
   * @return the update timestamp
   */
  Instant getUpdatedAt();
}
