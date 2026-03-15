package io.flowledger.domain.transaction.validator;

import io.flowledger.domain.transaction.entity.TransactionTag;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for TransactionTagWriteView.
 */
@Component
public class TransactionTagWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return TransactionTag.class;
  }
}
