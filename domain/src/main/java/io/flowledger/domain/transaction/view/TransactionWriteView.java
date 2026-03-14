package io.flowledger.domain.transaction.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.transaction.aggregate.Transaction;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for transactions.
 */
@EntityView(Transaction.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("transactionWrite")
public interface TransactionWriteView extends TransactionView {

  /**
   * Sets the transaction identifier.
   *
   * @param id the transaction id
   */
  void setId(UUID id);

  /**
   * Sets the account identifier.
   *
   * @param accountId the account id
   */
  void setAccountId(UUID accountId);

  /**
   * Sets the category identifier.
   *
   * @param categoryId the category id
   */
  void setCategoryId(UUID categoryId);

  /**
   * Sets the amount.
   *
   * @param amount the amount
   */
  void setAmount(BigDecimal amount);

  /**
   * Sets the currency code.
   *
   * @param currencyCode the currency code
   */
  void setCurrencyCode(String currencyCode);

  /**
   * Sets the transaction type.
   *
   * @param transactionType the transaction type
   */
  void setTransactionType(String transactionType);

  /**
   * Sets the status.
   *
   * @param status the status
   */
  void setStatus(String status);

  /**
   * Sets the description.
   *
   * @param description the description
   */
  void setDescription(String description);

  /**
   * Sets the location.
   *
   * @param location the location
   */
  void setLocation(String location);

  /**
   * Sets the occurrence timestamp.
   *
   * @param occurredAt the occurrence timestamp
   */
  void setOccurredAt(Instant occurredAt);

  /**
   * Sets the creation timestamp.
   *
   * @param createdAt the creation timestamp
   */
  void setCreatedAt(Instant createdAt);

  /**
   * Sets the update timestamp.
   *
   * @param updatedAt the update timestamp
   */
  void setUpdatedAt(Instant updatedAt);
}
