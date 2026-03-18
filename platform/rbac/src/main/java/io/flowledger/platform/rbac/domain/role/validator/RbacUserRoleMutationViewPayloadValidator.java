package io.flowledger.platform.rbac.domain.role.validator;

import io.flowledger.platform.rbac.domain.role.entity.RbacUserRole;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for RbacUserRoleMutationView.
 */
@Component
public class RbacUserRoleMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return RbacUserRole.class;
  }
}
