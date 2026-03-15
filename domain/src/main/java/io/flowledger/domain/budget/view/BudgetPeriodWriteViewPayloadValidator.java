package io.flowledger.domain.budget.view;

import io.flowledger.domain.budget.entity.BudgetPeriod;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for BudgetPeriodWriteView.
 */
@Component
public class BudgetPeriodWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

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
