package io.flowledger.platform.rbac.domain.permission.validator;

import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import io.flowledger.platform.rbac.domain.permission.entity.RbacRoleFieldActionPermission;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for RbacRoleFieldActionPermissionMutationView.
 */
@Component
public class RbacRoleFieldActionPermissionMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return RbacRoleFieldActionPermission.class;
  }
}
