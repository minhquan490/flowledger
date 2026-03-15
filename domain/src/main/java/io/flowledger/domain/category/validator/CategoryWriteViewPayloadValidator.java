package io.flowledger.domain.category.validator;

import io.flowledger.domain.category.aggregate.Category;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for CategoryWriteView.
 */
@Component
public class CategoryWriteViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
   */
  @Override
  protected Class<?> entityClass() {
    return Category.class;
  }
}
