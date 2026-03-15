package io.flowledger.domain.goal.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a goal reminder.
 */
@Entity
@Table(name = GoalReminder.TABLE_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalReminder {
  public static final String TABLE_NAME = "goal_reminders";

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "saving_goal_id", nullable = false)
  private UUID savingGoalId;

  @Column(name = "remind_at", nullable = false)
  private Instant remindAt;

  @Column(name = "channel", nullable = false, length = 50)
  private String channel;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
