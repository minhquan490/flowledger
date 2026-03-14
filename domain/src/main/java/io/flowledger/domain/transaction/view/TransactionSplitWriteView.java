package io.flowledger.domain.transaction.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.transaction.entity.TransactionSplit;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for transaction splits.
 */
@EntityView(TransactionSplit.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("transactionSplitWrite")
public interface TransactionSplitWriteView extends TransactionSplitView {

  /**
   * Sets the split identifier.
   *
   * @param id the split id
   */
  void setId(UUID id);

  /**
   * Sets the transaction identifier.
   *
   * @param transactionId the transaction id
   */
  void setTransactionId(UUID transactionId);

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
