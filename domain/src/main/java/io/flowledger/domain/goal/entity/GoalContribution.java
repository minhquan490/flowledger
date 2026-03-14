package io.flowledger.domain.goal.entity;

import java.math.BigDecimal;
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
 * Entity representing a contribution to a savings goal.
 */
@Entity
@Table(name = GoalContribution.TABLE_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalContribution {
  public static final String TABLE_NAME = "goal_contributions";

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "saving_goal_id", nullable = false)
  private UUID savingGoalId;

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  @Column(name = "contributed_at", nullable = false)
  private Instant contributedAt;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
