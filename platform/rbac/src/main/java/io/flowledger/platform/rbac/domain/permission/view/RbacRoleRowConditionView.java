package io.flowledger.platform.rbac.domain.permission.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.permission.entity.RbacRoleRowCondition;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;

import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for RBAC role row conditions.
 */
@EntityView(RbacRoleRowCondition.class)
@GraphQlModel(value = "rbacRoleRowCondition", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface RbacRoleRowConditionView {

  /**
   * Returns the condition identifier.
   *
   * @return the condition id
   */
  @IdMapping
  UUID getId();

  /**
   * Returns the role identifier.
   *
   * @return the role id
   */
  UUID getRoleId();

  /**
   * Returns the resource identifier.
   *
   * @return the resource id
   */
  UUID getResourceId();

  /**
   * Returns the condition JSON.
   *
   * @return the condition JSON
   */
  String getConditionJson();

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
