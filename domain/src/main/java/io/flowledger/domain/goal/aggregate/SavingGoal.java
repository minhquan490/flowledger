package io.flowledger.domain.goal.aggregate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
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
 * Aggregate root representing a savings goal.
 */
@Entity
@Table(name = SavingGoal.TABLE_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingGoal {
  public static final String TABLE_NAME = "saving_goals";

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "account_id", nullable = true)
  private UUID accountId;

  @Column(name = "name", nullable = false, length = 200)
  private String name;

  @Column(name = "target_amount", nullable = false, precision = 19, scale = 4)
  private BigDecimal targetAmount;

  @Column(name = "target_date", nullable = true)
  private LocalDate targetDate;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
