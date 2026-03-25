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
          "kind": "GROUP",
          "id": "g-root",
          "logicalOp": "AND",
          "children": [
            {
              "kind": "RULE",
              "id": "r-status",
              "field": "status",
              "op": "EQ",
              "value": "ACTIVE"
            },
            {
              "kind": "RULE",
              "id": "r-amount",
              "field": "amount",
              "op": "BETWEEN",
              "value": { "from": 10, "to": 20 }
            },
            {
              "kind": "GROUP",
              "id": "g-or",
              "logicalOp": "OR",
              "children": [
                {
                  "kind": "RULE",
                  "id": "r-priority",
                  "field": "priority",
                  "op": "GT",
                  "value": 3
                },
                {
                  "kind": "RULE",
                  "id": "r-tenant",
                  "field": "tenantId",
                  "op": "IN",
                  "value": ["t1", "t2"]
                }
              ]
            }
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
          "kind": "GROUP",
          "id": "g-root",
          "logicalOp": "AND",
          "children": [
            {
              "kind": "RULE",
              "id": "r-tenant",
              "field": "tenantId",
              "op": "EQ",
              "value": "t1"
            },
            {
              "kind": "GROUP",
              "id": "g-or",
              "logicalOp": "OR",
              "children": [
                {
                  "kind": "RULE",
                  "id": "r-status-a",
                  "field": "status",
                  "op": "EQ",
                  "value": "ACTIVE"
                },
                {
                  "kind": "RULE",
                  "id": "r-status-b",
                  "field": "status",
                  "op": "EQ",
                  "value": "PENDING"
                }
              ]
            }
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
          "kind": "GROUP",
          "id": "g-root",
          "logicalOp": "OR",
          "children": [
            {
              "kind": "RULE",
              "id": "r-status",
              "field": "status",
              "op": "EQ",
              "value": "ACTIVE"
            },
            {
              "kind": "GROUP",
              "id": "g-and",
              "logicalOp": "AND",
              "children": [
                {
                  "kind": "RULE",
                  "id": "r-type",
                  "field": "type",
                  "op": "EQ",
                  "value": "BANK"
                },
                {
                  "kind": "RULE",
                  "id": "r-country",
                  "field": "country",
                  "op": "EQ",
                  "value": "US"
                }
              ]
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
          "kind": "RULE",
          "id": "r-status",
          "field": "status",
          "op": "CONTAINS",
          "value": "ACTIVE"
        }
        """,
        "Unsupported operator: CONTAINS"
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
          "kind": "RULE",
          "id": "r-amount",
          "field": "amount",
          "op": "BETWEEN",
          "value": [1, 2, 3]
        }
        """,
        "BETWEEN must have exactly 2 items."
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
          "kind": "GROUP",
          "id": "g-root",
          "logicalOp": "OR",
          "children": []
        }
        """,
        "'children' must be non-empty."
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
