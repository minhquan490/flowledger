package io.flowledger.domain.recurring.validator;

import io.flowledger.domain.recurring.entity.RecurringOccurrence;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for RecurringOccurrenceMutationView.
 */
@Component
public class RecurringOccurrenceMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return RecurringOccurrence.class;
  }
}
