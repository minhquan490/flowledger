package io.flowledger.platform.rbac.domain.role.view;

import io.flowledger.platform.rbac.domain.role.entity.RbacRoleFieldPermission;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for RbacRoleFieldPermissionWriteView.
 */
@Component
public class RbacRoleFieldPermissionWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return RbacRoleFieldPermission.class;
  }
}
