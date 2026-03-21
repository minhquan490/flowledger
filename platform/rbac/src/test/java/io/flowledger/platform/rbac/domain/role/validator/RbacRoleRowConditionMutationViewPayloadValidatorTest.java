package io.flowledger.platform.rbac.domain.role.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowledger.platform.graphql.application.GraphQlValidationException;
import io.flowledger.platform.rbac.domain.permission.validator.RbacRoleRowConditionMutationViewPayloadValidator;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link RbacRoleRowConditionMutationViewPayloadValidator}.
 */
class RbacRoleRowConditionMutationViewPayloadValidatorTest {
  private static final String CONDITION_JSON_FIELD = "conditionJson";

  /**
   * Verifies that valid condition JSON is accepted.
   */
  @Test
  void validatePayloadAcceptsSupportedConditionJson() {
    assertValidCondition(
        """
        {
          "status": { "op": "eq", "value": "ACTIVE" },
          "amount": { "op": "between", "value": { "from": 10, "to": 20 } },
          "or": [
            { "priority": { "op": "gt", "value": 3 } },
            { "tenantId": { "op": "in", "value": ["t1", "t2"] } }
          ]
        }
        """
    );
  }

  /**
   * Verifies support for "field = value and (field = value or field = value)" shape.
   */
  @Test
  void validatePayloadAcceptsAndWithOrConditionShape() {
    assertValidCondition(
        """
        {
          "tenantId": { "op": "eq", "value": "t1" },
          "or": [
            { "status": { "op": "eq", "value": "ACTIVE" } },
            { "status": { "op": "eq", "value": "PENDING" } }
          ]
        }
        """
    );
  }

  /**
   * Verifies support for "field = value or (field = value and field = value)" shape.
   */
  @Test
  void validatePayloadAcceptsOrWithAndClauseConditionShape() {
    assertValidCondition(
        """
        {
          "or": [
            { "status": { "op": "eq", "value": "ACTIVE" } },
            {
              "type": { "op": "eq", "value": "BANK" },
              "country": { "op": "eq", "value": "US" }
            }
          ]
        }
        """
    );
  }

  /**
   * Verifies that invalid JSON is rejected.
   */
  @Test
  void validatePayloadRejectsInvalidJson() {
    assertInvalidCondition("{invalid", "must be valid JSON.");
  }

  /**
   * Verifies that unsupported operators are rejected.
   */
  @Test
  void validatePayloadRejectsUnsupportedOperator() {
    assertInvalidCondition(
        """
        {
          "status": { "op": "contains", "value": "ACTIVE" }
        }
        """,
        "Unsupported filter operator: contains"
    );
  }

  /**
   * Verifies that malformed between values are rejected.
   */
  @Test
  void validatePayloadRejectsMalformedBetweenValue() {
    assertInvalidCondition(
        """
        {
          "amount": { "op": "between", "value": [1, 2, 3] }
        }
        """,
        "BETWEEN array value must contain exactly 2 items."
    );
  }

  /**
   * Verifies that nested or blocks are rejected.
   */
  @Test
  void validatePayloadRejectsNestedOrClauses() {
    assertInvalidCondition(
        """
        {
          "or": [
            {
              "or": [
                { "status": { "op": "eq", "value": "ACTIVE" } }
              ]
            }
          ]
        }
        """,
        "'or' is only supported at root level."
    );
  }

  /**
   * Asserts that a condition JSON is valid.
   *
   * @param conditionJson the condition JSON
   */
  private void assertValidCondition(String conditionJson) {
    RbacRoleRowConditionMutationViewPayloadValidator validator = createValidator();
    Map<String, Object> payload = Map.of(CONDITION_JSON_FIELD, conditionJson);
    assertDoesNotThrow(() -> validator.validatePayload(payload));
  }

  /**
   * Asserts that a condition JSON is invalid with an expected error message.
   *
   * @param conditionJson the condition JSON
   * @param expectedMessage the expected validation message
   */
  private void assertInvalidCondition(String conditionJson, String expectedMessage) {
    RbacRoleRowConditionMutationViewPayloadValidator validator = createValidator();
    Map<String, Object> payload = Map.of(CONDITION_JSON_FIELD, conditionJson);
    GraphQlValidationException exception = assertThrows(
        GraphQlValidationException.class,
        () -> validator.validatePayload(payload)
    );
    assertEquals(expectedMessage, exception.getFieldErrors().get(CONDITION_JSON_FIELD));
  }

  /**
   * Creates the validator under test.
   *
   * @return validator instance
   */
  private RbacRoleRowConditionMutationViewPayloadValidator createValidator() {
    return new RbacRoleRowConditionMutationViewPayloadValidator(new ObjectMapper());
  }
}
