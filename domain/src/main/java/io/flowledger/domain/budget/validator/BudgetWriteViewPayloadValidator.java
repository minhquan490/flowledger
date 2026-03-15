package io.flowledger.domain.budget.validator;

import io.flowledger.domain.budget.aggregate.Budget;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for BudgetWriteView.
 */
@Component
public class BudgetWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return Budget.class;
  }
}
