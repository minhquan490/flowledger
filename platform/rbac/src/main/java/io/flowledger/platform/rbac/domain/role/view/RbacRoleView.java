package io.flowledger.platform.rbac.domain.role.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import io.flowledger.platform.rbac.domain.role.aggregate.RbacRole;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for RBAC roles.
 */
@EntityView(RbacRole.class)
@GraphQlModel(value = "rbacRole", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface RbacRoleView {

  /**
   * Returns the role identifier.
   *
   * @return the role id
   */
  UUID getId();

  /**
   * Returns the role code.
   *
   * @return the role code
   */
  String getCode();

  /**
   * Returns the role name.
   *
   * @return the role name
   */
  String getName();

  /**
   * Returns whether the role is the default role.
   *
   * @return true when the role is default
   */
  boolean isDefaultRole();

  /**
   * Returns whether the role is system-managed.
   *
   * @return true when system-managed
   */
  boolean isSystemManaged();

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
