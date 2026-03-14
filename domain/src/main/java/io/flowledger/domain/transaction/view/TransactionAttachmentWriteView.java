package io.flowledger.domain.transaction.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.transaction.entity.TransactionAttachment;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for transaction attachments.
 */
@EntityView(TransactionAttachment.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("transactionAttachmentWrite")
public interface TransactionAttachmentWriteView extends TransactionAttachmentView {

  /**
   * Sets the attachment identifier.
   *
   * @param id the attachment id
   */
  void setId(UUID id);

  /**
   * Sets the transaction identifier.
   *
   * @param transactionId the transaction id
   */
  void setTransactionId(UUID transactionId);

  /**
   * Sets the document identifier.
   *
   * @param documentId the document id
   */
  void setDocumentId(UUID documentId);

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
