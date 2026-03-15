package io.flowledger.domain.transaction.validator;

import io.flowledger.domain.transaction.entity.TransactionAttachment;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for TransactionAttachmentWriteView.
 */
@Component
public class TransactionAttachmentWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return TransactionAttachment.class;
  }
}
