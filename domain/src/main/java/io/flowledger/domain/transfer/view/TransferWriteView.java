package io.flowledger.domain.transfer.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.transfer.aggregate.Transfer;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for transfers.
 */
@EntityView(Transfer.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "transferWrite",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = TransferWriteViewPayloadValidator.class
)
public interface TransferWriteView extends TransferView {
  /**
   * Sets the source account identifier.
   *
   * @param fromAccountId the source account id
   */
  void setFromAccountId(UUID fromAccountId);

  /**
   * Sets the target account identifier.
   *
   * @param toAccountId the target account id
   */
  void setToAccountId(UUID toAccountId);

  /**
   * Sets the amount.
   *
   * @param amount the amount
   */
  void setAmount(BigDecimal amount);

  /**
   * Sets the fee amount.
   *
   * @param feeAmount the fee amount
   */
  void setFeeAmount(BigDecimal feeAmount);

  /**
   * Sets the status.
   *
   * @param status the status
   */
  void setStatus(String status);

  /**
   * Sets the scheduled timestamp.
   *
   * @param scheduledAt the scheduled timestamp
   */
  void setScheduledAt(Instant scheduledAt);

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
