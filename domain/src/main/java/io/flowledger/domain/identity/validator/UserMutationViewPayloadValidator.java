package io.flowledger.domain.identity.validator;

import io.flowledger.domain.identity.aggregate.User;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for UserMutationView.
 */
@Component
public class UserMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return User.class;
  }
}
