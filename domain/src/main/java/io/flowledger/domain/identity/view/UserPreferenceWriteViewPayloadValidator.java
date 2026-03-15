package io.flowledger.domain.identity.view;

import io.flowledger.domain.identity.entity.UserPreference;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for UserPreferenceWriteView.
 */
@Component
public class UserPreferenceWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return UserPreference.class;
  }
}
