package io.flowledger.domain.transaction.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.domain.transaction.entity.TransactionAttachment;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for transaction attachments.
 */
@EntityView(TransactionAttachment.class)
@GraphQlModel(value = "transactionAttachment", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface TransactionAttachmentView {

  /**
   * Returns the attachment identifier.
   *
   * @return the attachment id
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
   * Returns the document identifier.
   *
   * @return the document id
   */
  UUID getDocumentId();

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
