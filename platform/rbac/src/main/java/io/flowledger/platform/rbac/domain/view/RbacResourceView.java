package io.flowledger.platform.rbac.domain.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.entity.RbacResourceEntity;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for RBAC resources.
 */
@EntityView(RbacResourceEntity.class)
@GraphQlModel("rbacResource")
public interface RbacResourceView {

  /**
   * Returns the resource identifier.
   *
   * @return the resource id
   */
  UUID getId();

  /**
   * Returns the resource name.
   *
   * @return the resource name
   */
  String getName();

  /**
   * Returns the resource description.
   *
   * @return the resource description
   */
  String getDescription();

  /**
   * Returns whether the resource is system-managed.
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
