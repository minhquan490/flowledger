package io.flowledger.platform.rbac.domain.permission.valueobject;

import io.flowledger.platform.query.blaze.filter.BlazeFilterOperators;
import io.flowledger.platform.query.blaze.filter.LogicalOperator;

import java.util.List;

/**
 * Represents the structured payload stored in {@code condition_json} for row-level RBAC filtering.
 *
 * @param kind the node kind
 * @param id the unique node identifier
 * @param logicalOp the logical operator, only used when {@code kind == NodeKind.GROUP}
 * @param children the child nodes, only used when {@code kind == NodeKind.GROUP}
 * @param field the target field name, only used when {@code kind == NodeKind.RULE}
 * @param op the comparison operator, only used when {@code kind == NodeKind.RULE}
 * @param value the comparison value, only used when {@code kind == NodeKind.RULE}
 */
public record RbacRowConditionJson(
    NodeKind kind,
    String id,
    LogicalOperator logicalOp,
    List<RbacRowConditionJson> children,
    String field,
    BlazeFilterOperators op,
    Object value
) {

  /**
   * Defines available condition node kinds.
   */
  public enum NodeKind {
    GROUP,
    RULE
  }
}
