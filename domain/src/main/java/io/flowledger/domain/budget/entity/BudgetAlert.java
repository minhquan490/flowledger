package io.flowledger.domain.budget.entity;

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
 * Entity representing a budget alert configuration.
 */
@Entity
@Table(name = BudgetAlert.TABLE_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetAlert {
  public static final String TABLE_NAME = "budget_alerts";

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "budget_id", nullable = false)
  private UUID budgetId;

  @Column(name = "alert_type", nullable = false, length = 100)
  private String alertType;

  @Column(name = "threshold", nullable = true, precision = 19, scale = 4)
  private BigDecimal threshold;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
