package io.flowledger.domain.account.view;

import io.flowledger.domain.account.entity.AccountReconciliation;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for AccountReconciliationWriteView.
 */
@Component
public class AccountReconciliationWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

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
