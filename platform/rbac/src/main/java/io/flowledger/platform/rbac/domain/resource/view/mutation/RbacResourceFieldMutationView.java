package io.flowledger.platform.rbac.domain.resource.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.domain.resource.entity.RbacResourceField;
import io.flowledger.platform.rbac.domain.resource.validator.RbacResourceFieldMutationViewPayloadValidator;
import io.flowledger.platform.rbac.domain.resource.view.RbacResourceReferenceView;
import io.flowledger.platform.rbac.domain.resource.view.RbacResourceFieldView;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;
/**
 * Mutation-capable view for RBAC resource fields.
 */
@EntityView(RbacResourceField.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "rbacResourceFieldMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = RbacResourceFieldMutationViewPayloadValidator.class
)
public interface RbacResourceFieldMutationView extends RbacResourceFieldView {

  /**
   * Returns the resource view.
   *
   * @return the resource view
   */
  @Mapping("resource")
  RbacResourceReferenceView getResource();

  /**
   * Sets the resource view.
   *
   * @param resource the resource view
   */
  void setResource(RbacResourceReferenceView resource);

  /**
   * Sets the field name.
   *
   * @param fieldName the field name
   */
  void setFieldName(String fieldName);

  /**
   * Sets the source method name.
   *
   * @param sourceMethodName the source method name
   */
  void setSourceMethodName(String sourceMethodName);

  /**
   * Returns the creation timestamp.
   *
   * @return the creation timestamp
   */
  Instant getCreatedAt();

  /**
   * Sets the creation timestamp.
   *
   * @param createdAt the creation timestamp
   */
  void setCreatedAt(Instant createdAt);

  /**
   * Returns the update timestamp.
   *
   * @return the update timestamp
   */
  Instant getUpdatedAt();

  /**
   * Sets the update timestamp.
   *
   * @param updatedAt the update timestamp
   */
  void setUpdatedAt(Instant updatedAt);
}
