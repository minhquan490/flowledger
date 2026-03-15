package io.flowledger.domain.recurring.aggregate;

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
 * Aggregate root representing a recurring transaction rule.
 */
@Entity
@Table(name = RecurringRule.TABLE_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecurringRule {
  public static final String TABLE_NAME = "recurring_rules";

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "name", nullable = false, length = 200)
  private String name;

  @Column(name = "schedule", nullable = false, length = 100)
  private String schedule;

  @Column(name = "status", nullable = false, length = 50)
  private String status;

  @Column(name = "next_run_at", nullable = true)
  private Instant nextRunAt;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
