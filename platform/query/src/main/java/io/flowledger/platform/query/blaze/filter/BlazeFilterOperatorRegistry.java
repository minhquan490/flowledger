package io.flowledger.platform.query.blaze.filter;

import com.blazebit.persistence.BaseWhereBuilder;
import io.flowledger.platform.query.QuerySystemException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Registry that resolves and applies filter operator strategies.
 */
public class BlazeFilterOperatorRegistry {
  private final Map<String, BlazeFilterOperatorStrategy> strategiesByOperator;

  /**
   * Creates a registry with a strategy list.
   *
   * @param strategies supported strategies
   */
  public BlazeFilterOperatorRegistry(List<BlazeFilterOperatorStrategy> strategies) {
    this.strategiesByOperator = strategies.stream()
        .collect(Collectors.toUnmodifiableMap(
            strategy -> normalizeOperator(strategy.operator()),
            Function.identity()
        ));
  }

  /**
   * Creates a default registry with built-in operators.
   *
   * @return default operator registry
   */
  public static BlazeFilterOperatorRegistry defaultRegistry() {
    return new BlazeFilterOperatorRegistry(List.of(
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.EQ, ComparisonBlazeFilterOperatorStrategy.ComparisonMode.EQ),
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.NE, ComparisonBlazeFilterOperatorStrategy.ComparisonMode.NOT_EQ),
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.NEQ, ComparisonBlazeFilterOperatorStrategy.ComparisonMode.NOT_EQ),
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.GT, ComparisonBlazeFilterOperatorStrategy.ComparisonMode.GT),
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.GE, ComparisonBlazeFilterOperatorStrategy.ComparisonMode.GE),
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.GTE, ComparisonBlazeFilterOperatorStrategy.ComparisonMode.GE),
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.LT, ComparisonBlazeFilterOperatorStrategy.ComparisonMode.LT),
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.LE, ComparisonBlazeFilterOperatorStrategy.ComparisonMode.LE),
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.LTE, ComparisonBlazeFilterOperatorStrategy.ComparisonMode.LE),
        new BetweenBlazeFilterOperatorStrategy(BlazeFilterOperators.BETWEEN, false),
        new BetweenBlazeFilterOperatorStrategy(BlazeFilterOperators.NOT_BETWEEN, true),
        new BetweenBlazeFilterOperatorStrategy(BlazeFilterOperators.N_BETWEEN, true),
        new InBlazeFilterOperatorStrategy(BlazeFilterOperators.IN, false),
        new InBlazeFilterOperatorStrategy(BlazeFilterOperators.NOT_IN, true),
        new InBlazeFilterOperatorStrategy(BlazeFilterOperators.NIN, true),
        new LikeBlazeFilterOperatorStrategy(BlazeFilterOperators.LIKE, false, true),
        new LikeBlazeFilterOperatorStrategy(BlazeFilterOperators.NOT_LIKE, true, true),
        new LikeBlazeFilterOperatorStrategy(BlazeFilterOperators.ILIKE, false, false),
        new LikeBlazeFilterOperatorStrategy(BlazeFilterOperators.NOT_ILIKE, true, false),
        new NullityBlazeFilterOperatorStrategy(BlazeFilterOperators.IS_NULL, false),
        new NullityBlazeFilterOperatorStrategy(BlazeFilterOperators.IS_NOT_NULL, true)
    ));
  }

  /**
   * Applies an operator using a matching strategy.
   *
   * @param operator raw operator name
   * @param whereBuilder target where builder
   * @param field field name
   * @param value operator value
   */
  public void apply(String operator, BaseWhereBuilder<?> whereBuilder, String field, Object value) {
    BlazeFilterOperatorStrategy strategy = strategiesByOperator.get(normalizeOperator(operator));
    if (strategy == null) {
      throw new QuerySystemException("Unsupported filter operator: " + operator);
    }
    strategy.apply(whereBuilder, field, value);
  }

  /**
   * Normalizes operator names to lower case.
   *
   * @param operator raw operator
   * @return normalized operator
   */
  private static String normalizeOperator(String operator) {
    return BlazeFilterOperators.normalize(operator);
  }
}
