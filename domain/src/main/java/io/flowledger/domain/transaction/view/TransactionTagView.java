package io.flowledger.domain.transaction.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.transaction.entity.TransactionTag;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for transaction tags.
 */
@EntityView(TransactionTag.class)
@GraphQlModel("transactionTag")
public interface TransactionTagView {

  /**
   * Returns the transaction tag identifier.
   *
   * @return the tag id
   */
  UUID getId();

  /**
   * Returns the transaction identifier.
   *
   * @return the transaction id
   */
  UUID getTransactionId();

  /**
   * Returns the tag identifier.
   *
   * @return the tag id
   */
  UUID getTagId();

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
