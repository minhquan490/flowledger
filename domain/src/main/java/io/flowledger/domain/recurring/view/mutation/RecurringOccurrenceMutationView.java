package io.flowledger.domain.recurring.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.recurring.entity.RecurringOccurrence;
import io.flowledger.domain.recurring.validator.RecurringOccurrenceMutationViewPayloadValidator;
import io.flowledger.domain.recurring.view.RecurringOccurrenceView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * Mutation-capable view for recurring occurrences.
 */
@EntityView(RecurringOccurrence.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "recurringOccurrenceMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = RecurringOccurrenceMutationViewPayloadValidator.class
)
public interface RecurringOccurrenceMutationView extends RecurringOccurrenceView {
  /**
   * Sets the recurring rule identifier.
   *
   * @param recurringRuleId the recurring rule id
   */
  void setRecurringRuleId(UUID recurringRuleId);

  /**
   * Sets the scheduled timestamp.
   *
   * @param scheduledAt the scheduled timestamp
   */
  void setScheduledAt(Instant scheduledAt);

  /**
   * Sets the status.
   *
   * @param status the status
   */
  void setStatus(String status);

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
