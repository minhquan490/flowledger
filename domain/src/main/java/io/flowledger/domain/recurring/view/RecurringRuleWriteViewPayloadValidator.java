package io.flowledger.domain.recurring.view;

import io.flowledger.domain.recurring.aggregate.RecurringRule;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for RecurringRuleWriteView.
 */
@Component
public class RecurringRuleWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

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
