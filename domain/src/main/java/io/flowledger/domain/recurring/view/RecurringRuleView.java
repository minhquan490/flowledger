package io.flowledger.domain.recurring.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.recurring.aggregate.RecurringRule;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for recurring rules.
 */
@EntityView(RecurringRule.class)
@GraphQlModel(value = "recurringRule", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface RecurringRuleView {

  /**
   * Returns the recurring rule identifier.
   *
   * @return the recurring rule id
   */
  UUID getId();

  /**
   * Returns the recurring rule name.
   *
   * @return the recurring rule name
   */
  String getName();

  /**
   * Returns the owning user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the schedule.
   *
   * @return the schedule
   */
  String getSchedule();

  /**
   * Returns the status.
   *
   * @return the status
   */
  String getStatus();

  /**
   * Returns the next run timestamp.
   *
   * @return the next run timestamp
   */
  Instant getNextRunAt();

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
