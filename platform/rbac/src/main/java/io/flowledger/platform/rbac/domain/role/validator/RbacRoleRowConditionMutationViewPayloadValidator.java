package io.flowledger.platform.rbac.domain.role.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowledger.platform.graphql.application.GraphQlValidationException;
import io.flowledger.platform.graphql.application.JpaSchemaGraphQlMutationPayloadValidator;
import io.flowledger.platform.query.blaze.filter.BlazeFilterOperators;
import io.flowledger.platform.query.blaze.filter.BlazeFilterSyntax;
import io.flowledger.platform.rbac.domain.role.entity.RbacRoleRowCondition;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validates mutation payloads for RbacRoleRowConditionMutationView.
 */
@Component
@RequiredArgsConstructor
public class RbacRoleRowConditionMutationViewPayloadValidator extends JpaSchemaGraphQlMutationPayloadValidator {
  private static final String CONDITION_JSON_FIELD = "conditionJson";
  private static final Pattern FIELD_PATTERN = Pattern.compile("^[A-Za-z_][A-Za-z0-9_.]*$");
  private static final int BAD_REQUEST_STATUS = 400;

  private final ObjectMapper objectMapper;

  /**
   * Returns the entity class used for schema validation.
   *
   * @return the entity class
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
    Object conditionJson = payload.get(CONDITION_JSON_FIELD);
    if (conditionJson == null || errors.containsKey(CONDITION_JSON_FIELD)) {
      return;
    }
    if (!(conditionJson instanceof String conditionJsonString)) {
      errors.put(CONDITION_JSON_FIELD, "must be a JSON string.");
      return;
    }
    String trimmedConditionJson = conditionJsonString.trim();
    if (trimmedConditionJson.isEmpty()) {
      errors.put(CONDITION_JSON_FIELD, "must not be blank.");
      return;
    }
    try {
      JsonNode rootNode = objectMapper.readTree(trimmedConditionJson);
      validateConditionRoot(rootNode);
    } catch (JsonProcessingException _) {
      errors.put(CONDITION_JSON_FIELD, "must be valid JSON.");
    } catch (GraphQlValidationException ex) {
      errors.put(CONDITION_JSON_FIELD, ex.getFieldErrors().get(CONDITION_JSON_FIELD));
    }
  }

  /**
   * Validates the root condition object.
   *
   * @param rootNode root json node
   */
  private void validateConditionRoot(JsonNode rootNode) {
    if (rootNode == null || !rootNode.isObject()) {
      throwConditionValidation("must be a JSON object.");
    }
    validateConditionObject(rootNode, true);
  }

  /**
   * Validates a filter object against supported keys.
   *
   * @param objectNode filter object node
   * @param allowLogicalOr indicates whether logical-or key is allowed
   */
  private void validateConditionObject(JsonNode objectNode, boolean allowLogicalOr) {
    if (!objectNode.isObject()) {
      throwConditionValidation("must be a JSON object.");
    }
    Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
    while (fields.hasNext()) {
      Map.Entry<String, JsonNode> fieldEntry = fields.next();
      String fieldName = fieldEntry.getKey();
      JsonNode fieldValue = fieldEntry.getValue();
      if (isLogicalOrKey(fieldName)) {
        if (!allowLogicalOr) {
          throwConditionValidation("'or' is only supported at root level.");
        }
        validateLogicalOrNode(fieldValue);
        continue;
      }
      validateFieldName(fieldName);
      validateFieldPredicate(fieldValue);
    }
  }

  /**
   * Validates the logical-or node.
   *
   * @param logicalOrNode logical-or node
   */
  private void validateLogicalOrNode(JsonNode logicalOrNode) {
    if (logicalOrNode == null || !logicalOrNode.isArray() || logicalOrNode.isEmpty()) {
      throwConditionValidation("'or' must be a non-empty array of objects.");
    }
    for (JsonNode clauseNode : logicalOrNode) {
      if (clauseNode == null || !clauseNode.isObject()) {
        throwConditionValidation("Each 'or' clause must be an object.");
      }
      validateConditionObject(clauseNode, false);
    }
  }

  /**
   * Validates a single field predicate value.
   *
   * @param predicateNode predicate node
   */
  private void validateFieldPredicate(JsonNode predicateNode) {
    if (!isOperatorObject(predicateNode)) {
      return;
    }
    String operator = resolveOperator(predicateNode);
    boolean hasValue = predicateNode.has(BlazeFilterSyntax.OPERATOR_VALUE_KEY);
    if (BlazeFilterOperators.VALUE_REQUIRED_OPERATORS.contains(operator) && !hasValue) {
      throwConditionValidation("Operator '" + operator + "' requires a 'value'.");
    }
    if (BlazeFilterOperators.VALUE_OPTIONAL_OPERATORS.contains(operator)) {
      return;
    }
    JsonNode valueNode = predicateNode.get(BlazeFilterSyntax.OPERATOR_VALUE_KEY);
    if (BlazeFilterOperators.BETWEEN_OPERATORS.contains(operator)) {
      validateBetweenValue(valueNode);
    }
  }

  /**
   * Validates between operator value shape.
   *
   * @param valueNode between value node
   */
  private void validateBetweenValue(JsonNode valueNode) {
    if (valueNode == null || valueNode.isNull()) {
      throwConditionValidation("BETWEEN operator requires a non-null value.");
    }
    if (valueNode.isArray()) {
      if (valueNode.size() != 2) {
        throwConditionValidation("BETWEEN array value must contain exactly 2 items.");
      }
      return;
    }
    if (valueNode.isObject()) {
      if (!valueNode.has(BlazeFilterSyntax.BETWEEN_FROM_KEY)
          || !valueNode.has(BlazeFilterSyntax.BETWEEN_TO_KEY)) {
        throwConditionValidation("BETWEEN object value must contain 'from' and 'to'.");
      }
      return;
    }
    throwConditionValidation("BETWEEN value must be an object or a 2-item array.");
  }

  /**
   * Resolves and validates operator name.
   *
   * @param predicateNode predicate node
   * @return normalized operator
   */
  private String resolveOperator(JsonNode predicateNode) {
    JsonNode operatorNode = predicateNode.get(BlazeFilterSyntax.OPERATOR_KEY);
    if (operatorNode == null || !operatorNode.isTextual() || operatorNode.asText().isBlank()) {
      throwConditionValidation("Filter operator must be a non-empty string.");
    }
    String operator = BlazeFilterOperators.normalize(operatorNode.asText());
    if (!BlazeFilterOperators.SUPPORTED_OPERATORS.contains(operator)) {
      throwConditionValidation("Unsupported filter operator: " + operator);
    }
    return operator;
  }

  /**
   * Validates field naming constraints.
   *
   * @param fieldName field name
   */
  private void validateFieldName(String fieldName) {
    if (fieldName == null || fieldName.isBlank()) {
      throwConditionValidation("Filter field name must be non-empty.");
    }
    if (!FIELD_PATTERN.matcher(fieldName).matches()) {
      throwConditionValidation("Invalid filter field name: " + fieldName);
    }
  }

  /**
   * Indicates whether a node is an explicit operator object.
   *
   * @param predicateNode candidate node
   * @return true when node is an operator object
   */
  private boolean isOperatorObject(JsonNode predicateNode) {
    return predicateNode != null
        && predicateNode.isObject()
        && predicateNode.has(BlazeFilterSyntax.OPERATOR_KEY);
  }

  /**
   * Indicates whether a key represents logical-or.
   *
   * @param key key name
   * @return true when key is logical-or marker
   */
  private boolean isLogicalOrKey(String key) {
    return BlazeFilterSyntax.LOGICAL_OR.equalsIgnoreCase(key)
        || BlazeFilterSyntax.LOGICAL_OR_ALIAS.equalsIgnoreCase(key);
  }

  /**
   * Throws a field-specific validation exception for conditionJson.
   *
   * @param message validation message
   */
  private void throwConditionValidation(String message) {
    throw new GraphQlValidationException(
        "Mutation payload validation failed.",
        BAD_REQUEST_STATUS,
        Map.of(CONDITION_JSON_FIELD, message)
    );
  }
}
