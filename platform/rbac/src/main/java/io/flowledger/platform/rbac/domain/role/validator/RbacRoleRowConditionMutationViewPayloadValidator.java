package io.flowledger.platform.rbac.domain.role.validator;

import io.flowledger.platform.rbac.domain.role.entity.RbacRoleRowCondition;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for RbacRoleRowConditionMutationView.
 */
@Component
public class RbacRoleRowConditionMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return RbacRoleRowCondition.class;
  }
}
