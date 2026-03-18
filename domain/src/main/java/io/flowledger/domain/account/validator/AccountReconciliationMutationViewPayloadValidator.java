package io.flowledger.domain.account.validator;

import io.flowledger.domain.account.entity.AccountReconciliation;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for AccountReconciliationMutationView.
 */
@Component
public class AccountReconciliationMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return AccountReconciliation.class;
  }
}
