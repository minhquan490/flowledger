package io.flowledger.platform.rbac.domain.resource.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import io.flowledger.platform.rbac.domain.resource.entity.RbacResourceField;

import java.util.UUID;

/**
 * GraphQL view for RBAC resource fields.
 */
@EntityView(RbacResourceField.class)
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
  @Mapping("resource.id")
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
