package io.flowledger.domain.transfer.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.transfer.aggregate.Transfer;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for transfers.
 */
@EntityView(Transfer.class)
@GraphQlModel("transfer")
public interface TransferView {

  /**
   * Returns the transfer identifier.
   *
   * @return the transfer id
   */
  UUID getId();

  /**
   * Returns the source account identifier.
   *
   * @return the source account id
   */
  UUID getFromAccountId();

  /**
   * Returns the target account identifier.
   *
   * @return the target account id
   */
  UUID getToAccountId();

  /**
   * Returns the transfer amount.
   *
   * @return the amount
   */
  BigDecimal getAmount();

  /**
   * Returns the fee amount.
   *
   * @return the fee amount
   */
  BigDecimal getFeeAmount();

  /**
   * Returns the transfer status.
   *
   * @return the status
   */
  String getStatus();

  /**
   * Returns the scheduled timestamp.
   *
   * @return the scheduled timestamp
   */
  Instant getScheduledAt();

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
