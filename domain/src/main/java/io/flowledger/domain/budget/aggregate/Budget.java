package io.flowledger.domain.budget.aggregate;

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
 * Aggregate root representing a budget.
 */
@Entity
@Table(name = Budget.TABLE_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Budget {
  public static final String TABLE_NAME = "budgets";

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "category_id")
  private UUID categoryId;

  @Column(name = "account_id")
  private UUID accountId;

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  @Column(name = "period_type", nullable = false)
  private String periodType;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
