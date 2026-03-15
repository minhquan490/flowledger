package io.flowledger.platform.graphql.application;

import jakarta.persistence.Column;
import org.springframework.beans.factory.DisposableBean;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.flowledger.platform.graphql.application.GraphQlQueryService.BAD_REQUEST_STATUS;

/**
 * Base validator that checks payload fields against JPA column metadata.
 */
public abstract class JpaSchemaGraphQlMutationPayloadValidator
    implements GraphQLMutationPayloadValidator, DisposableBean {

  private static final Map<Class<?>, Map<String, ColumnMetadata>> COLUMN_CACHE =
      new ConcurrentHashMap<>();

  /**
   * Returns the JPA entity class that defines the table schema.
   */
  protected abstract Class<?> entityClass();

  /**
   * Validates payload fields against column constraints.
   */
  @Override
  public void validatePayload(Map<String, Object> payload) throws GraphQlApiException {

    if (isEmpty(payload)) {
      return;
    }

    Map<String, ColumnMetadata> metadata = getColumnMetadata();
    Map<String, String> errors = new LinkedHashMap<>();

    payload.forEach((field, value) ->
        validateColumn(field, value, metadata.get(field), errors));

    validateBusinessRules(payload, errors);

    if (!errors.isEmpty()) {
      throw new GraphQlValidationException(
          "Mutation payload validation failed.",
          BAD_REQUEST_STATUS,
          errors
      );
    }
  }

  /**
   * Hook for custom business validation.
   */
  protected void validateBusinessRules(Map<String, Object> payload, Map<String, String> errors) {
  }

  @Override
  public void destroy() {
    COLUMN_CACHE.clear();
  }

  private void validateColumn(
      String field,
      Object value,
      ColumnMetadata column,
      Map<String, String> errors
  ) {

    if (column == null) {
      return;
    }

    validateNullability(field, value, column, errors);
    validateLength(field, value, column, errors);
  }

  private void validateNullability(
      String field,
      Object value,
      ColumnMetadata column,
      Map<String, String> errors
  ) {
    if (value == null && !column.nullable()) {
      errors.put(field, "must not be null");
    }
  }

  private void validateLength(
      String field,
      Object value,
      ColumnMetadata column,
      Map<String, String> errors
  ) {

    if (!(value instanceof String stringValue)) {
      return;
    }

    int maxLength = column.length();

    if (maxLength > 0 && stringValue.length() > maxLength) {
      errors.put(field, "exceeds max length " + maxLength);
    }
  }

  private Map<String, ColumnMetadata> getColumnMetadata() {
    return COLUMN_CACHE.computeIfAbsent(entityClass(), this::buildMetadata);
  }

  private Map<String, ColumnMetadata> buildMetadata(Class<?> entityType) {

    Map<String, ColumnMetadata> metadata = new ConcurrentHashMap<>();

    for (Field field : entityType.getDeclaredFields()) {

      Column column = field.getAnnotation(Column.class);

      if (column == null) {
        continue;
      }

      metadata.put(
          field.getName(),
          new ColumnMetadata(column.nullable(), column.length())
      );
    }

    return metadata;
  }

  private boolean isEmpty(Map<?, ?> map) {
    return map == null || map.isEmpty();
  }

  /**
   * Column constraint metadata derived from JPA annotations.
   */
  private record ColumnMetadata(boolean nullable, int length) {}
}
