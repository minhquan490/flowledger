package io.flowledger.domain.transaction.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.transaction.entity.TransactionTag;
import io.flowledger.domain.transaction.validator.TransactionTagMutationViewPayloadValidator;
import io.flowledger.domain.transaction.view.TransactionTagView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * Mutation-capable view for transaction tags.
 */
@EntityView(TransactionTag.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "transactionTagMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = TransactionTagMutationViewPayloadValidator.class
)
public interface TransactionTagMutationView extends TransactionTagView {
  /**
   * Sets the transaction identifier.
   *
   * @param transactionId the transaction id
   */
  void setTransactionId(UUID transactionId);

  /**
   * Sets the tag identifier.
   *
   * @param tagId the tag id
   */
  void setTagId(UUID tagId);

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
