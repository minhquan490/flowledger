package io.flowledger.domain.goal.validator;

import io.flowledger.domain.goal.entity.GoalReminder;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for GoalReminderWriteView.
 */
@Component
public class GoalReminderWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return GoalReminder.class;
  }
}
