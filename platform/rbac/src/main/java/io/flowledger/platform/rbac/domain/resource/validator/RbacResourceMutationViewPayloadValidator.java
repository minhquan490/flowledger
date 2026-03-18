package io.flowledger.platform.rbac.domain.resource.validator;

import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for RbacResourceMutationView.
 */
@Component
public class RbacResourceMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return RbacResource.class;
  }
}
