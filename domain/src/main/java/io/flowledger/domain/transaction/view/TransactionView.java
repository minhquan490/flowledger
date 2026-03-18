package io.flowledger.domain.transaction.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.domain.transaction.aggregate.Transaction;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for transactions.
 */
@EntityView(Transaction.class)
@GraphQlModel(value = "transaction", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface TransactionView {

  /**
   * Returns the transaction identifier.
   *
   * @return the transaction id
   */
  @IdMapping
  UUID getId();

  /**
   * Returns the account identifier.
   *
   * @return the account id
   */
  UUID getAccountId();

  /**
   * Returns the category identifier.
   *
   * @return the category id
   */
  UUID getCategoryId();

  /**
   * Returns the transaction amount.
   *
   * @return the amount
   */
  BigDecimal getAmount();

  /**
   * Returns the currency code.
   *
   * @return the currency code
   */
  String getCurrencyCode();

  /**
   * Returns the transaction type.
   *
   * @return the transaction type
   */
  String getTransactionType();

  /**
   * Returns the transaction status.
   *
   * @return the status
   */
  String getStatus();

  /**
   * Returns the description.
   *
   * @return the description
   */
  String getDescription();

  /**
   * Returns the location.
   *
   * @return the location
   */
  String getLocation();

  /**
   * Returns the occurrence timestamp.
   *
   * @return the occurrence timestamp
   */
  Instant getOccurredAt();

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
