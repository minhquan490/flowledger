package io.flowledger.domain.account.aggregate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import io.flowledger.domain.identity.aggregate.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Aggregate root representing a financial account.
 */
@Entity
@Table(name = Account.TABLE_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
  public static final String TABLE_NAME = "accounts";

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
  private User user;

  @Column(name = "name", nullable = false, length = 200)
  private String name;

  @Column(name = "account_type", nullable = false, length = 100)
  private String accountType;

  @Column(name = "currency_code", nullable = false, length = 10)
  private String currencyCode;

  @Column(name = "opening_balance", nullable = false, precision = 19, scale = 4)
  private BigDecimal openingBalance;

  @Column(name = "status", nullable = false, length = 50)
  private String status;

  @Column(name = "icon", nullable = true, length = 200)
  private String icon;

  @Column(name = "color", nullable = true, length = 20)
  private String color;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
