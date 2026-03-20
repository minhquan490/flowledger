package io.flowledger.domain.transaction.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.transaction.entity.TransactionAttachment;
import io.flowledger.domain.transaction.validator.TransactionAttachmentMutationViewPayloadValidator;
import io.flowledger.domain.transaction.view.TransactionAttachmentView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * Mutation-capable view for transaction attachments.
 */
@EntityView(TransactionAttachment.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "transactionAttachmentMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = TransactionAttachmentMutationViewPayloadValidator.class
)
public interface TransactionAttachmentMutationView extends TransactionAttachmentView {
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
