package io.flowledger.domain.transaction.validator;

import io.flowledger.domain.transaction.entity.TransactionSplit;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for TransactionSplitWriteView.
 */
@Component
public class TransactionSplitWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return TransactionSplit.class;
  }
}
