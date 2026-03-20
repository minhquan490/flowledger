package io.flowledger.platform.query.blaze.filter;

import com.blazebit.persistence.BaseWhereBuilder;
import io.flowledger.platform.query.QuerySystemException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Filter operator strategy for {@code BETWEEN}/{@code NOT BETWEEN} predicates.
 *
 * <p>Example JSON:
 * <pre>{@code
 * {
 *   "createdAt": { "op": "between", "value": { "from": "2026-01-01", "to": "2026-01-31" } },
 *   "amount": { "op": "between", "value": [100, 500] },
 *   "priority": { "op": "notBetween", "value": [3, 5] }
 * }
 * }</pre>
 */
public class BetweenBlazeFilterOperatorStrategy implements BlazeFilterOperatorStrategy {
  private final String operator;
  private final boolean negated;

  /**
   * Creates a between strategy.
   *
   * @param operator operator keyword
   * @param negated whether to apply {@code NOT BETWEEN}
   */
  public BetweenBlazeFilterOperatorStrategy(String operator, boolean negated) {
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
   * Applies a between predicate.
   *
   * @param whereBuilder the target where builder
   * @param field the field name
   * @param value range input value
   */
  @Override
  public void apply(BaseWhereBuilder<?> whereBuilder, String field, Object value) {
    RangeBound rangeBound = resolveRangeBound(value);
    if (negated) {
      whereBuilder.where(field).notBetween(rangeBound.from()).and(rangeBound.to());
      return;
    }
    whereBuilder.where(field).between(rangeBound.from()).and(rangeBound.to());
  }

  /**
   * Resolves range bounds from an operator value.
   *
   * @param value raw operator value
   * @return resolved range bound
   */
  private RangeBound resolveRangeBound(Object value) {
    if (value instanceof Map<?, ?> mapValue) {
      Object from = mapValue.get(BlazeFilterSyntax.BETWEEN_FROM_KEY);
      Object to = mapValue.get(BlazeFilterSyntax.BETWEEN_TO_KEY);
      if (from == null || to == null) {
        throw new QuerySystemException("BETWEEN filter map must contain 'from' and 'to'.");
      }
      return new RangeBound(from, to);
    }
    if (value instanceof Collection<?> collectionValue) {
      List<?> values = collectionValue.stream().toList();
      if (values.size() != 2) {
        throw new QuerySystemException("BETWEEN filter collection must contain exactly 2 values.");
      }
      return new RangeBound(values.get(0), values.get(1));
    }
    if (value != null && value.getClass().isArray()) {
      Object[] values = (Object[]) value;
      if (values.length != 2) {
        throw new QuerySystemException("BETWEEN filter array must contain exactly 2 values.");
      }
      return new RangeBound(values[0], values[1]);
    }
    throw new QuerySystemException("BETWEEN filter must be a map or 2-item collection.");
  }

  /**
   * Immutable value object for a between range.
   *
   * @param from lower bound
   * @param to upper bound
   */
  private record RangeBound(Object from, Object to) {
  }
}
