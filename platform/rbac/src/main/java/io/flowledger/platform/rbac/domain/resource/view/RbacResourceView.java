package io.flowledger.platform.rbac.domain.resource.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for RBAC resources.
 */
@EntityView(RbacResource.class)
@GraphQlModel(value = "rbacResource", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface RbacResourceView {

  /**
   * Returns the resource identifier.
   *
   * @return the resource id
   */
  @IdMapping
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
