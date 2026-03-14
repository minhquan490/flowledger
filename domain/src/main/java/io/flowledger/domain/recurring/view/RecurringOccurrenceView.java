package io.flowledger.domain.recurring.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.recurring.entity.RecurringOccurrence;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for recurring occurrences.
 */
@EntityView(RecurringOccurrence.class)
@GraphQlModel("recurringOccurrence")
public interface RecurringOccurrenceView {

  /**
   * Returns the occurrence identifier.
   *
   * @return the occurrence id
   */
  UUID getId();

  /**
   * Returns the recurring rule identifier.
   *
   * @return the recurring rule id
   */
  UUID getRecurringRuleId();

  /**
   * Returns the scheduled timestamp.
   *
   * @return the scheduled timestamp
   */
  Instant getScheduledAt();

  /**
   * Returns the status.
   *
   * @return the status
   */
  String getStatus();

  /**
   * Returns the creation timestamp.
   *
   * @return the creation timestamp
   */
  Instant getCreatedAt();

  /**
   * Returns the update timestamp.
   *
   * @return the update timestamp
   */
  Instant getUpdatedAt();
}
