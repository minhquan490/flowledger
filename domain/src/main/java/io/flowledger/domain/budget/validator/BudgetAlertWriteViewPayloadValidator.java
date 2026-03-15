package io.flowledger.domain.budget.validator;

import io.flowledger.domain.budget.entity.BudgetAlert;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for BudgetAlertWriteView.
 */
@Component
public class BudgetAlertWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return BudgetAlert.class;
  }
}
