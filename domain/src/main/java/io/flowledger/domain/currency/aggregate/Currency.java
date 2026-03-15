package io.flowledger.domain.currency.aggregate;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Aggregate root representing a supported currency.
 */
@Entity
@Table(name = Currency.TABLE_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
  public static final String TABLE_NAME = "currencies";

  @Id
  @Column(name = "code", nullable = false, length = 10)
  private String code;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "symbol", nullable = true, length = 10)
  private String symbol;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
