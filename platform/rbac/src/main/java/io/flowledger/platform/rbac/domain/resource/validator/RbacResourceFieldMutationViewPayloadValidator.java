package io.flowledger.platform.rbac.domain.resource.validator;

import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import io.flowledger.platform.rbac.domain.resource.entity.RbacResourceField;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for RbacResourceFieldMutationView.
 */
@Component
public class RbacResourceFieldMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return RbacResourceField.class;
  }
}
