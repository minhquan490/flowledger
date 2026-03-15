package io.flowledger.domain.transaction.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.transaction.entity.TransactionSplit;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for transaction splits.
 */
@EntityView(TransactionSplit.class)
@GraphQlModel(value = "transactionSplit", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface TransactionSplitView {

  /**
   * Returns the split identifier.
   *
   * @return the split id
   */
  UUID getId();

  /**
   * Returns the transaction identifier.
   *
   * @return the transaction id
   */
  UUID getTransactionId();

  /**
   * Returns the category identifier.
   *
   * @return the category id
   */
  UUID getCategoryId();

  /**
   * Returns the split amount.
   *
   * @return the amount
   */
  BigDecimal getAmount();

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
