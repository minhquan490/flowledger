package io.flowledger.platform.query.blaze.filter;

import com.blazebit.persistence.BaseWhereBuilder;

/**
 * Filter operator strategy for {@code LIKE} family predicates.
 *
 * <p>Example JSON:
 * <pre>{@code
 * {
 *   "name": { "op": "like", "value": "%Ledger%" },
 *   "email": { "op": "ilike", "value": "%@flowledger.io" },
 *   "code": { "op": "notLike", "value": "TMP%" },
 *   "title": { "op": "notIlike", "value": "%draft%" }
 * }
 * }</pre>
 */
public class LikeBlazeFilterOperatorStrategy implements BlazeFilterOperatorStrategy {
  private final String operator;
  private final boolean negated;
  private final boolean caseSensitive;

  /**
   * Creates a like strategy.
   *
   * @param operator operator keyword
   * @param negated whether to apply a negated predicate
   * @param caseSensitive whether matching is case sensitive
   */
  public LikeBlazeFilterOperatorStrategy(String operator, boolean negated, boolean caseSensitive) {
    this.operator = operator;
    this.negated = negated;
    this.caseSensitive = caseSensitive;
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
   * Applies a like predicate.
   *
   * @param whereBuilder the target where builder
   * @param field the field name
   * @param value the right-hand pattern value
   */
  @Override
  public void apply(BaseWhereBuilder<?> whereBuilder, String field, Object value) {
    if (negated) {
      whereBuilder.where(field).notLike(caseSensitive).value(value);
      return;
    }
    whereBuilder.where(field).like(caseSensitive).value(value);
  }
}
