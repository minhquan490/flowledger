package io.flowledger.domain.recurring.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.recurring.aggregate.RecurringRule;
import io.flowledger.domain.recurring.validator.RecurringRuleWriteViewPayloadValidator;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for recurring rules.
 */
@EntityView(RecurringRule.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "recurringRuleWrite",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = RecurringRuleWriteViewPayloadValidator.class
)
public interface RecurringRuleWriteView extends RecurringRuleView {
  /**
   * Sets the user identifier.
   *
   * @param userId the user id
   */
  void setUserId(UUID userId);

  /**
   * Sets the name.
   *
   * @param name the name
   */
  void setName(String name);

  /**
   * Sets the schedule.
   *
   * @param schedule the schedule
   */
  void setSchedule(String schedule);

  /**
   * Sets the status.
   *
   * @param status the status
   */
  void setStatus(String status);

  /**
   * Sets the next run timestamp.
   *
   * @param nextRunAt the next run timestamp
   */
  void setNextRunAt(Instant nextRunAt);

  /**
   * Sets the creation timestamp.
   *
   * @param createdAt the creation timestamp
   */
  void setCreatedAt(Instant createdAt);

  /**
   * Sets the update timestamp.
   *
   * @param updatedAt the update timestamp
   */
  void setUpdatedAt(Instant updatedAt);
}
