package io.flowledger.domain.budget.validator;

import io.flowledger.domain.budget.entity.BudgetPeriod;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for BudgetPeriodMutationView.
 */
@Component
public class BudgetPeriodMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return BudgetPeriod.class;
  }
}
