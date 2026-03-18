package io.flowledger.domain.transaction.validator;

import io.flowledger.domain.transaction.entity.TransactionAttachment;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for TransactionAttachmentMutationView.
 */
@Component
public class TransactionAttachmentMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

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
