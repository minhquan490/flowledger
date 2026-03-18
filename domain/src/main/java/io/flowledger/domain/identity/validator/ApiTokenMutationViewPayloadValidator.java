package io.flowledger.domain.identity.validator;

import io.flowledger.domain.identity.entity.ApiToken;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for ApiTokenMutationView.
 */
@Component
public class ApiTokenMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

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
