package io.flowledger.platform.rbac.domain.permission.validator;

import tools.jackson.databind.ObjectMapper;
import io.flowledger.core.web.HttpStatusCodes;
import io.flowledger.platform.graphql.application.GraphQlValidationException;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import io.flowledger.platform.query.QuerySystemException;
import io.flowledger.platform.query.blaze.filter.BlazeFilterOperators;
import io.flowledger.platform.rbac.domain.permission.entity.RbacRoleRowCondition;
import io.flowledger.platform.rbac.domain.permission.valueobject.RbacRowConditionJson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Validates mutation payloads for row-condition updates using {@link RbacRowConditionJson}.
 *
 * <p>Ensures the JSON conforms to the row-condition schema and operator constraints.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class RbacRoleRowConditionMutationViewPayloadValidator
    extends JpaSchemaGraphQlMutationPayloadValidator {

  private static final String FIELD = "conditionJson";
  private static final Pattern FIELD_PATTERN = Pattern.compile("^[A-Za-z_][A-Za-z0-9_.]*$");

  private final ObjectMapper objectMapper;

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the RBAC row condition entity class
   */
  @Override
  protected Class<?> entityClass() {
    return RbacRoleRowCondition.class;
  }

  /**
   * Validates business rules for the mutation payload.
   *
   * @param payload mutation payload
   * @param errors validation errors keyed by field
   */
  @Override
  protected void validateBusinessRules(Map<String, Object> payload, Map<String, String> errors) {
    clearLengthError(errors);
    Object raw = payload.get(FIELD);

    if (shouldSkip(raw, errors)) return;

    String json = extractJson(raw, errors);
    if (json == null) return;

    RbacRowConditionJson root = parse(json, errors);
    if (root == null) return;

    validateNode(root);
  }

  /**
   * Determines whether validation should be skipped for the field.
   *
   * @param raw raw field value
   * @param errors current error map
   * @return true when validation should be skipped
   */
  private boolean shouldSkip(Object raw, Map<String, String> errors) {
    return raw == null || errors.containsKey(FIELD);
  }

  /**
   * Extracts and trims the JSON string from the raw value.
   *
   * @param raw raw field value
   * @param errors current error map
   * @return trimmed JSON string or null when invalid
   */
  private String extractJson(Object raw, Map<String, String> errors) {
    if (!(raw instanceof String json)) {
      errors.put(FIELD, "must be a JSON string.");
      return null;
    }

    String trimmed = json.trim();
    if (trimmed.isEmpty()) {
      errors.put(FIELD, "must not be blank.");
      return null;
    }

    return trimmed;
  }

  /**
   * Parses the JSON payload into a {@link RbacRowConditionJson}.
   *
   * @param json JSON string
   * @param errors current error map
   * @return parsed condition or null when invalid
   */
  private RbacRowConditionJson parse(String json, Map<String, String> errors) {
    try {
      return objectMapper.readValue(json, RbacRowConditionJson.class);
    } catch (Exception e) {
      String errorMessage = resolveJsonErrorMessage(e);
      log.error("Failed to parse JSON string.", e);
      errors.put(FIELD, errorMessage);
      return null;
    }
  }

  /**
   * Clears schema-derived length errors for the condition JSON field.
   *
   * @param errors current error map
   */
  private void clearLengthError(Map<String, String> errors) {
    String error = errors.get(FIELD);
    if (error != null && error.startsWith("exceeds max length")) {
      errors.remove(FIELD);
    }
  }

  /**
   * Resolves a JSON parsing error message, preserving operator errors when possible.
   *
   * @param exception parsing exception
   * @return user-facing validation message
   */
  private String resolveJsonErrorMessage(Exception exception) {
    QuerySystemException operatorError = findOperatorError(exception);
    if (operatorError != null) {
      return normalizeOperatorMessage(operatorError.getMessage());
    }
    return "must be valid JSON.";
  }

  /**
   * Finds a query system exception in a throwable chain.
   *
   * @param exception root exception
   * @return the query system exception or null when absent
   */
  private QuerySystemException findOperatorError(Exception exception) {
    Throwable current = exception;
    while (current != null) {
      if (current instanceof QuerySystemException querySystemException) {
        return querySystemException;
      }
      current = current.getCause();
    }
    return null;
  }

  /**
   * Normalizes operator errors for validation output.
   *
   * @param message operator error message
   * @return normalized error message
   */
  private String normalizeOperatorMessage(String message) {
    if (message == null) {
      return "Unsupported operator.";
    }
    if (message.startsWith("Unsupported filter operator: ")) {
      return "Unsupported operator: " + message.substring("Unsupported filter operator: ".length());
    }
    return message;
  }

  /**
   * Validates a condition node based on its kind.
   *
   * @param node condition node
   */
  private void validateNode(RbacRowConditionJson node) {
    require(node != null, "must be a JSON object.");
    require(node.kind() != null, "'kind' is required.");
    require(notBlank(node.id()), "'id' must be non-empty.");

    switch (node.kind()) {
      case GROUP -> validateGroup(node);
      case RULE -> validateRule(node);
      default -> throwError("Unsupported node kind: " + node.kind());
    }
  }

  /**
   * Validates a group node.
   *
   * @param node group node
   */
  private void validateGroup(RbacRowConditionJson node) {
    require(node.logicalOp() != null, "'logicalOp' is required for GROUP.");
    require(notEmpty(node.children()), "'children' must be non-empty.");

    node.children().forEach(child -> {
      require(child != null, "GROUP children must be non-null.");
      validateNode(child);
    });
  }

  /**
   * Validates a rule node.
   *
   * @param node rule node
   */
  private void validateRule(RbacRowConditionJson node) {
    require(notBlank(node.field()), "'field' is required.");
    validateField(node.field());

    require(node.op() != null, "'op' is required.");
    validateOperator(node.op());

    validateValue(node);
  }

  /**
   * Validates that the operator is supported.
   *
   * @param op operator value
   */
  private void validateOperator(BlazeFilterOperators op) {
    if (!BlazeFilterOperators.SUPPORTED_OPERATORS.contains(op)) {
      throwError("Unsupported operator: " + op);
    }
  }

  /**
   * Validates the rule value based on operator requirements.
   *
   * @param node rule node
   */
  private void validateValue(RbacRowConditionJson node) {
    var op = node.op();
    var value = node.value();

    if (BlazeFilterOperators.VALUE_REQUIRED_OPERATORS.contains(op)) {
      require(value != null, "Operator '" + op.keyword() + "' requires value.");
    }

    if (BlazeFilterOperators.BETWEEN_OPERATORS.contains(op)) {
      validateBetween(value);
    }
  }

  /**
   * Validates BETWEEN operator value shape.
   *
   * @param value between value
   */
  private void validateBetween(Object value) {
    require(value != null, "BETWEEN requires value.");

    if (value instanceof Iterable<?> it) {
      require(size(it) == 2, "BETWEEN must have exactly 2 items.");
      return;
    }

    if (value instanceof Object[] arr) {
      require(arr.length == 2, "BETWEEN array must have 2 items.");
      return;
    }

    if (value instanceof Map<?, ?> map) {
      require(map.containsKey("from") && map.containsKey("to"),
          "BETWEEN object must contain 'from' and 'to'.");
      return;
    }

    throwError("Invalid BETWEEN value format.");
  }

  /**
   * Validates field name constraints.
   *
   * @param field field name
   */
  private void validateField(String field) {
    require(FIELD_PATTERN.matcher(field).matches(),
        "Invalid field name: " + field);
  }

  /**
   * Throws a validation error when a condition is false.
   *
   * @param condition condition to enforce
   * @param message error message
   */
  private void require(boolean condition, String message) {
    if (!condition) throwError(message);
  }

  /**
   * Throws a field-specific validation exception.
   *
   * @param message validation message
   */
  private void throwError(String message) {
    throw new GraphQlValidationException(
        "Mutation payload validation failed.",
        HttpStatusCodes.BAD_REQUEST,
        Map.of(FIELD, message)
    );
  }

  /**
   * Checks whether a string is non-null and non-blank.
   *
   * @param s string value
   * @return true when non-blank
   */
  private boolean notBlank(String s) {
    return s != null && !s.isBlank();
  }

  /**
   * Checks whether an iterable has at least one element.
   *
   * @param it iterable value
   * @return true when non-empty
   */
  private boolean notEmpty(Iterable<?> it) {
    return it != null && it.iterator().hasNext();
  }

  /**
   * Counts elements in an iterable.
   *
   * @param it iterable value
   * @return number of elements
   */
  private int size(Iterable<?> it) {
    int count = 0;
    for (var _ : it) count++;
    return count;
  }
}
