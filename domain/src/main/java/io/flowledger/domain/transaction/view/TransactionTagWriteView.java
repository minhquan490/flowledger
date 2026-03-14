package io.flowledger.domain.transaction.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.transaction.entity.TransactionTag;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for transaction tags.
 */
@EntityView(TransactionTag.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("transactionTagWrite")
public interface TransactionTagWriteView extends TransactionTagView {

  /**
   * Sets the tag identifier.
   *
   * @param id the tag id
   */
  void setId(UUID id);

  /**
   * Sets the transaction identifier.
   *
   * @param transactionId the transaction id
   */
  void setTransactionId(UUID transactionId);

  /**
   * Sets the tag identifier.
   *
   * @param tagId the tag id
   */
  void setTagId(UUID tagId);

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
