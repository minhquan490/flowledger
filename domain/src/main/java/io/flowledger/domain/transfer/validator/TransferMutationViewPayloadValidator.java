package io.flowledger.domain.transfer.validator;

import io.flowledger.domain.transfer.aggregate.Transfer;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for TransferMutationView.
 */
@Component
public class TransferMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return Transfer.class;
  }
}
