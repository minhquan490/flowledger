package io.flowledger.domain.identity.view;

import io.flowledger.domain.identity.entity.ApiToken;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for ApiTokenWriteView.
 */
@Component
public class ApiTokenWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return ApiToken.class;
  }
}
