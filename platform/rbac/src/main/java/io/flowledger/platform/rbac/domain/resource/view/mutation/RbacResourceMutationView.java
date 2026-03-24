package io.flowledger.platform.rbac.domain.resource.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import io.flowledger.platform.rbac.domain.resource.validator.RbacResourceMutationViewPayloadValidator;
import io.flowledger.platform.rbac.domain.resource.view.RbacResourceView;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;

/**
 * Mutation-capable view for RBAC resources.
 */
@EntityView(RbacResource.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "rbacResourceMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = RbacResourceMutationViewPayloadValidator.class
)
public interface RbacResourceMutationView extends RbacResourceView {

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
   * Sets the creation timestamp.
   *
   * @param createdAt the creation timestamp
   */
  void setCreatedAt(java.time.Instant createdAt);

  /**
   * Sets the update timestamp.
   *
   * @param updatedAt the update timestamp
   */
  void setUpdatedAt(java.time.Instant updatedAt);
}
