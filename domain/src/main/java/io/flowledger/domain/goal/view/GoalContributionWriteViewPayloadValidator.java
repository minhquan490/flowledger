package io.flowledger.domain.goal.view;

import io.flowledger.domain.goal.entity.GoalContribution;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for GoalContributionWriteView.
 */
@Component
public class GoalContributionWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return GoalContribution.class;
  }
}
