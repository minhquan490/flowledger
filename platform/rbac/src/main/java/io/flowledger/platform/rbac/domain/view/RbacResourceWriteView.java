package io.flowledger.platform.rbac.domain.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.entity.RbacResourceEntity;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for RBAC resources.
 */
@EntityView(RbacResourceEntity.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("rbacResourceWrite")
public interface RbacResourceWriteView extends RbacResourceView {

  /**
   * Sets the resource identifier.
   *
   * @param id the resource id
   */
  void setId(UUID id);

  /**
   * Sets the resource name.
   *
   * @param name the resource name
   */
  void setName(String name);

  /**
   * Sets the resource description.
   *
   * @param description the resource description
   */
  void setDescription(String description);

  /**
   * Sets whether the resource is system-managed.
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
