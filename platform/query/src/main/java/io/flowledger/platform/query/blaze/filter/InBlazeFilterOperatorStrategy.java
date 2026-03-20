package io.flowledger.platform.query.blaze.filter;

import com.blazebit.persistence.BaseWhereBuilder;
import java.util.Collection;
import java.util.List;

/**
 * Filter operator strategy for {@code IN}/{@code NOT IN} predicates.
 *
 * <p>Example JSON:
 * <pre>{@code
 * {
 *   "orgId": { "op": "in", "value": ["org-1", "org-2"] },
 *   "status": { "op": "notIn", "value": ["ARCHIVED", "DELETED"] }
 * }
 * }</pre>
 */
public class InBlazeFilterOperatorStrategy implements BlazeFilterOperatorStrategy {
  private final String operator;
  private final boolean negated;

  /**
   * Creates an {@code IN} family strategy.
   *
   * @param operator operator keyword
   * @param negated whether to apply {@code NOT IN}
   */
  public InBlazeFilterOperatorStrategy(String operator, boolean negated) {
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
   * Applies an {@code IN}/{@code NOT IN} predicate.
   *
   * @param whereBuilder the target where builder
   * @param field the field name
   * @param value the right-hand values container
   */
  @Override
  public void apply(BaseWhereBuilder<?> whereBuilder, String field, Object value) {
    Collection<?> values = normalizeValues(value);
    if (negated) {
      whereBuilder.where(field).notIn(values);
      return;
    }
    whereBuilder.where(field).in(values);
  }

  /**
   * Normalizes raw values to a collection.
   *
   * @param value raw operator value
   * @return collection of values
   */
  private Collection<?> normalizeValues(Object value) {
    if (value == null) {
      return List.of();
    }
    if (value instanceof Collection<?> values) {
      return values;
    }
    if (value.getClass().isArray()) {
      return List.of((Object[]) value);
    }
    return List.of(value);
  }
}
