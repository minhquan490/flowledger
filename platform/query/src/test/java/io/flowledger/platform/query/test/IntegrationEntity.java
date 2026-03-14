package io.flowledger.platform.query.test;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * Simple entity used for integration testing.
 */
@Entity
@Table(name = "integration_entity")
public class IntegrationEntity {
  @Id
  private Long id;
  private BigDecimal amount;

  /**
   * Returns the entity id.
   *
   * @return id value
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the entity id.
   *
   * @param id id value
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Returns the amount value.
   *
   * @return amount
   */
  public BigDecimal getAmount() {
    return amount;
  }

  /**
   * Sets the amount value.
   *
   * @param amount amount
   */
  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}
