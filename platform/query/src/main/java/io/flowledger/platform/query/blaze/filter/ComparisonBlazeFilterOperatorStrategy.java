package io.flowledger.platform.query.blaze.filter;

import com.blazebit.persistence.BaseWhereBuilder;

/**
 * Filter operator strategy for comparison-style predicates.
 *
 * <p>Example JSON:
 * <pre>{@code
 * {
 *   "amount": { "op": "eq", "value": 100 },
 *   "status": { "op": "ne", "value": "DELETED" },
 *   "score": { "op": "gt", "value": 80 },
 *   "age": { "op": "gte", "value": 18 },
 *   "rank": { "op": "lt", "value": 10 },
 *   "balance": { "op": "lte", "value": 5000 }
 * }
 * }</pre>
 */
public class ComparisonBlazeFilterOperatorStrategy implements BlazeFilterOperatorStrategy {
  private final String operator;
  private final ComparisonMode mode;

  /**
   * Creates a comparison strategy.
   *
   * @param operator operator keyword
   * @param mode comparison behavior
   */
  public ComparisonBlazeFilterOperatorStrategy(String operator, ComparisonMode mode) {
    this.operator = operator;
    this.mode = mode;
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
   * Applies a comparison predicate.
   *
   * @param whereBuilder the target where builder
   * @param field the field name
   * @param value the right-hand value
   */
  @Override
  public void apply(BaseWhereBuilder<?> whereBuilder, String field, Object value) {
    switch (mode) {
      case EQ -> whereBuilder.where(field).eq(value);
      case NOT_EQ -> whereBuilder.where(field).notEq(value);
      case GT -> whereBuilder.where(field).gt(value);
      case GE -> whereBuilder.where(field).ge(value);
      case LT -> whereBuilder.where(field).lt(value);
      case LE -> whereBuilder.where(field).le(value);
    }
  }

  /**
   * Comparison behaviors supported by this strategy.
   */
  public enum ComparisonMode {
    EQ,
    NOT_EQ,
    GT,
    GE,
    LT,
    LE
  }
}
