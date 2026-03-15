package io.flowledger.domain.account.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.account.entity.AccountReconciliation;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for account reconciliations.
 */
@EntityView(AccountReconciliation.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "accountReconciliationWrite",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = AccountReconciliationWriteViewPayloadValidator.class
)
public interface AccountReconciliationWriteView extends AccountReconciliationView {
  /**
   * Sets the account identifier.
   *
   * @param accountId the account id
   */
  void setAccountId(UUID accountId);

  /**
   * Sets the reconciliation timestamp.
   *
   * @param reconciledAt the timestamp
   */
  void setReconciledAt(Instant reconciledAt);

  /**
   * Sets the notes.
   *
   * @param notes the notes
   */
  void setNotes(String notes);

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
