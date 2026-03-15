package io.flowledger.domain.transfer.aggregate;

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
 * Aggregate root representing a transfer between accounts.
 */
@Entity
@Table(name = Transfer.TABLE_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
  public static final String TABLE_NAME = "transfers";

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "from_account_id", nullable = false)
  private UUID fromAccountId;

  @Column(name = "to_account_id", nullable = false)
  private UUID toAccountId;

  @Column(name = "amount", nullable = false, precision = 19, scale = 4)
  private BigDecimal amount;

  @Column(name = "fee_amount", nullable = true, precision = 19, scale = 4)
  private BigDecimal feeAmount;

  @Column(name = "status", nullable = false, length = 50)
  private String status;

  @Column(name = "scheduled_at", nullable = true)
  private Instant scheduledAt;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
