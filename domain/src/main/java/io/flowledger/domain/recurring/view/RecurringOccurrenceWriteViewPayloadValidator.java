package io.flowledger.domain.recurring.view;

import io.flowledger.domain.recurring.entity.RecurringOccurrence;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for RecurringOccurrenceWriteView.
 */
@Component
public class RecurringOccurrenceWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

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
