package io.flowledger.domain.goal.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.goal.entity.GoalReminder;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for goal reminders.
 */
@EntityView(GoalReminder.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("goalReminderWrite")
public interface GoalReminderWriteView extends GoalReminderView {

  /**
   * Sets the reminder identifier.
   *
   * @param id the reminder id
   */
  void setId(UUID id);

  /**
   * Sets the saving goal identifier.
   *
   * @param savingGoalId the saving goal id
   */
  void setSavingGoalId(UUID savingGoalId);

  /**
   * Sets the reminder timestamp.
   *
   * @param remindAt the reminder timestamp
   */
  void setRemindAt(Instant remindAt);

  /**
   * Sets the reminder channel.
   *
   * @param channel the channel
   */
  void setChannel(String channel);

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
