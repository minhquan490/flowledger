package io.flowledger.platform.rbac.domain.role.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.resource.entity.RbacResourceField;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;

import java.util.UUID;

/**
 * GraphQL view for RBAC resource fields.
 */
@EntityView(RbacResourceField.class)
@GraphQlModel(value = "rbacResourceField", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface RbacResourceFieldView {

  /**
   * Returns the resource field identifier.
   *
   * @return the resource field id
   */
  @IdMapping
  UUID getId();

  /**
   * Returns the resource identifier.
   *
   * @return the resource id
   */
  UUID getResourceId();

  /**
   * Returns the field name.
   *
   * @return the field name
   */
  String getFieldName();

  /**
   * Returns the source method name.
   *
   * @return the source method name
   */
  String getSourceMethodName();
}
