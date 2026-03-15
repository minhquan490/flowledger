package io.flowledger.domain.budget.entity;

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
 * Entity capturing budget history snapshots.
 */
@Entity
@Table(name = BudgetHistory.TABLE_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetHistory {
  public static final String TABLE_NAME = "budget_history";

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "budget_id", nullable = false)
  private UUID budgetId;

  @Column(name = "snapshot_date", nullable = false)
  private LocalDate snapshotDate;

  @Column(name = "spent_amount", nullable = false, precision = 19, scale = 4)
  private BigDecimal spentAmount;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
