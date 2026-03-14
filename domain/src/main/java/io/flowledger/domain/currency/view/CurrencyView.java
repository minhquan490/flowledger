package io.flowledger.domain.currency.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.currency.aggregate.Currency;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;

/**
 * GraphQL view for currencies.
 */
@EntityView(Currency.class)
@GraphQlModel("currency")
public interface CurrencyView {

  /**
   * Returns the currency code.
   *
   * @return the currency code
   */
  String getCode();

  /**
   * Returns the currency name.
   *
   * @return the currency name
   */
  String getName();

  /**
   * Returns the currency symbol.
   *
   * @return the currency symbol
   */
  String getSymbol();

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
