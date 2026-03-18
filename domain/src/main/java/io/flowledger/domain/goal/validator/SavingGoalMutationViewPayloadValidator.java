package io.flowledger.domain.goal.validator;

import io.flowledger.domain.goal.aggregate.SavingGoal;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for SavingGoalMutationView.
 */
@Component
public class SavingGoalMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return SavingGoal.class;
  }
}
