package io.flowledger.domain.goal.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.goal.entity.GoalReminder;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for goal reminders.
 */
@EntityView(GoalReminder.class)
@GraphQlModel(value = "goalReminder", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface GoalReminderView {

  /**
   * Returns the reminder identifier.
   *
   * @return the reminder id
   */
  UUID getId();

  /**
   * Returns the saving goal identifier.
   *
   * @return the saving goal id
   */
  UUID getSavingGoalId();

  /**
   * Returns the reminder timestamp.
   *
   * @return the reminder timestamp
   */
  Instant getRemindAt();

  /**
   * Returns the reminder channel.
   *
   * @return the channel
   */
  String getChannel();

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
