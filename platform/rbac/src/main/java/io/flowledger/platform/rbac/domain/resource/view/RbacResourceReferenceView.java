package io.flowledger.platform.rbac.domain.resource.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import java.util.UUID;

/**
 * Lightweight reference view for RBAC resource associations.
 */
@EntityView(RbacResource.class)
public interface RbacResourceReferenceView {

  /**
   * Returns the resource identifier.
   *
   * @return the resource id
   */
  @IdMapping
  UUID getId();
}
