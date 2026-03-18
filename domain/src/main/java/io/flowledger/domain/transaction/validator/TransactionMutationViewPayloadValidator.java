package io.flowledger.domain.transaction.validator;

import io.flowledger.domain.transaction.aggregate.Transaction;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for TransactionMutationView.
 */
@Component
public class TransactionMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return Transaction.class;
  }
}
