package io.flowledger.platform.rbac.domain.resource.view;

import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for RbacResourceWriteView.
 */
@Component
public class RbacResourceWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

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
