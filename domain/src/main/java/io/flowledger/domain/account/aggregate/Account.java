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

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "account_type", nullable = false)
  private String accountType;

  @Column(name = "currency_code", nullable = false)
  private String currencyCode;

  @Column(name = "opening_balance", nullable = false)
  private BigDecimal openingBalance;

  @Column(name = "status", nullable = false)
  private String status;

  @Column(name = "icon")
  private String icon;

  @Column(name = "color")
  private String color;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
