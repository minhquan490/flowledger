package io.flowledger.domain.account.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.account.entity.AccountReconciliation;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for account reconciliations.
 */
@EntityView(AccountReconciliation.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("accountReconciliationWrite")
public interface AccountReconciliationWriteView extends AccountReconciliationView {

  /**
   * Sets the reconciliation identifier.
   *
   * @param id the reconciliation id
   */
  void setId(UUID id);

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
