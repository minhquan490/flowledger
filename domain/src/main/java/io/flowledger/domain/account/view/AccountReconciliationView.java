package io.flowledger.domain.account.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.account.entity.AccountReconciliation;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;

import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for account reconciliations.
 */
@EntityView(AccountReconciliation.class)
@GraphQlModel(value = "accountReconciliation", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface AccountReconciliationView {

  /**
   * Returns the reconciliation identifier.
   *
   * @return the reconciliation id
   */
  UUID getId();

  /**
   * Returns the account identifier.
   *
   * @return the account id
   */
  UUID getAccountId();

  /**
   * Returns the reconciliation timestamp.
   *
   * @return the reconciled at timestamp
   */
  Instant getReconciledAt();

  /**
   * Returns the reconciliation notes.
   *
   * @return the notes
   */
  String getNotes();

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
