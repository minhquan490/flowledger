package io.flowledger.domain.recurring.validator;

import io.flowledger.domain.recurring.aggregate.RecurringRule;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for RecurringRuleMutationView.
 */
@Component
public class RecurringRuleMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return RecurringRule.class;
  }
}
