package io.flowledger.platform.rbac.domain.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.entity.RbacRoleRowConditionEntity;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for RBAC role row conditions.
 */
@EntityView(RbacRoleRowConditionEntity.class)
@GraphQlModel("rbacRoleRowCondition")
public interface RbacRoleRowConditionView {

  /**
   * Returns the condition identifier.
   *
   * @return the condition id
   */
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
   * Returns whether the condition is system-managed.
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
