package io.flowledger.domain.identity.validator;

import io.flowledger.domain.identity.entity.UserSession;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for UserSessionWriteView.
 */
@Component
public class UserSessionWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return UserSession.class;
  }
}
