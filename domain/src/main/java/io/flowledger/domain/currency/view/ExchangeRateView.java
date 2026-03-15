package io.flowledger.domain.currency.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.currency.entity.ExchangeRate;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * GraphQL view for exchange rates.
 */
@EntityView(ExchangeRate.class)
@GraphQlModel(value = "exchangeRate", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface ExchangeRateView {

  /**
   * Returns the exchange rate identifier.
   *
   * @return the exchange rate id
   */
  UUID getId();

  /**
   * Returns the base currency code.
   *
   * @return the base currency code
   */
  String getBaseCurrencyCode();

  /**
   * Returns the quote currency code.
   *
   * @return the quote currency code
   */
  String getQuoteCurrencyCode();

  /**
   * Returns the rate value.
   *
   * @return the rate value
   */
  BigDecimal getRate();

  /**
   * Returns the rate date.
   *
   * @return the rate date
   */
  LocalDate getRateDate();

  /**
   * Returns the source.
   *
   * @return the source
   */
  String getSource();

  /**
   * Returns the creation timestamp.
   *
   * @return the creation timestamp
   */
  Instant getCreatedAt();

  /**
   * Returns the update timestamp.
   *
   * @return the update timestamp
   */
  Instant getUpdatedAt();
}
