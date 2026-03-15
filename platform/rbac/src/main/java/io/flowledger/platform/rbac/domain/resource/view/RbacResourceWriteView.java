package io.flowledger.platform.rbac.domain.resource.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import java.time.Instant;

/**
 * Write-capable view for RBAC resources.
 */
@EntityView(RbacResource.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "rbacResourceWrite",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = RbacResourceWriteViewPayloadValidator.class
)
public interface RbacResourceWriteView extends RbacResourceView {

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
