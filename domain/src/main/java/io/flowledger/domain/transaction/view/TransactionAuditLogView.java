package io.flowledger.domain.transaction.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.domain.transaction.entity.TransactionAuditLog;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for transaction audit logs.
 */
@EntityView(TransactionAuditLog.class)
@GraphQlModel(value = "transactionAuditLog", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface TransactionAuditLogView {

  /**
   * Returns the audit log identifier.
   *
   * @return the audit log id
   */
  @IdMapping
  UUID getId();

  /**
   * Returns the transaction identifier.
   *
   * @return the transaction id
   */
  UUID getTransactionId();

  /**
   * Returns the audit action.
   *
   * @return the action
   */
  String getAction();

  /**
   * Returns the audit details.
   *
   * @return the details
   */
  String getDetails();

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
