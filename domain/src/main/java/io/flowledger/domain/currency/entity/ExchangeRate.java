package io.flowledger.domain.currency.entity;

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
 * Entity representing a currency exchange rate.
 */
@Entity
@Table(name = ExchangeRate.TABLE_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
  public static final String TABLE_NAME = "exchange_rates";

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "base_currency_code", nullable = false, length = 10)
  private String baseCurrencyCode;

  @Column(name = "quote_currency_code", nullable = false, length = 10)
  private String quoteCurrencyCode;

  @Column(name = "rate", nullable = false, precision = 19, scale = 6)
  private BigDecimal rate;

  @Column(name = "rate_date", nullable = false)
  private LocalDate rateDate;

  @Column(name = "source", nullable = true, length = 100)
  private String source;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
