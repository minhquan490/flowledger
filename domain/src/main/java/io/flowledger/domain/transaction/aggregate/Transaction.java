package io.flowledger.domain.transaction.aggregate;

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
 * Aggregate root representing a financial transaction.
 */
@Entity
@Table(name = Transaction.TABLE_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
  public static final String TABLE_NAME = "transactions";

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "account_id", nullable = false)
  private UUID accountId;

  @Column(name = "category_id", nullable = true)
  private UUID categoryId;

  @Column(name = "amount", nullable = false, precision = 19, scale = 4)
  private BigDecimal amount;

  @Column(name = "currency_code", nullable = false, length = 10)
  private String currencyCode;

  @Column(name = "transaction_type", nullable = false, length = 50)
  private String transactionType;

  @Column(name = "status", nullable = false, length = 50)
  private String status;

  @Column(name = "description", nullable = true, length = 500)
  private String description;

  @Column(name = "location", nullable = true, length = 200)
  private String location;

  @Column(name = "occurred_at", nullable = false)
  private Instant occurredAt;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
