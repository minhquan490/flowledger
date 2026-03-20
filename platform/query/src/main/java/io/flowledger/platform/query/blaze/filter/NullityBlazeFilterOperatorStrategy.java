package io.flowledger.platform.query.blaze.filter;

import com.blazebit.persistence.BaseWhereBuilder;

/**
 * Filter operator strategy for nullity predicates.
 *
 * <p>Example JSON:
 * <pre>{@code
 * {
 *   "deletedAt": { "op": "isNull" },
 *   "approvedAt": { "op": "isNotNull" }
 * }
 * }</pre>
 */
public class NullityBlazeFilterOperatorStrategy implements BlazeFilterOperatorStrategy {
  private final String operator;
  private final boolean negated;

  /**
   * Creates a nullity strategy.
   *
   * @param operator operator keyword
   * @param negated whether to apply {@code IS NOT NULL}
   */
  public NullityBlazeFilterOperatorStrategy(String operator, boolean negated) {
    this.operator = operator;
    this.negated = negated;
  }

  /**
   * Returns the configured operator keyword.
   *
   * @return operator keyword
   */
  @Override
  public String operator() {
    return operator;
  }

  /**
   * Applies an {@code IS NULL}/{@code IS NOT NULL} predicate.
   *
   * @param whereBuilder the target where builder
   * @param field the field name
   * @param value ignored value for compatibility
   */
  @Override
  public void apply(BaseWhereBuilder<?> whereBuilder, String field, Object value) {
    if (negated) {
      whereBuilder.where(field).isNotNull();
      return;
    }
    whereBuilder.where(field).isNull();
  }
}
