package io.flowledger.platform.rbac.domain.role.validator;

import io.flowledger.platform.rbac.domain.role.aggregate.RbacRole;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for RbacRoleMutationView.
 */
@Component
public class RbacRoleMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return RbacRole.class;
  }
}
