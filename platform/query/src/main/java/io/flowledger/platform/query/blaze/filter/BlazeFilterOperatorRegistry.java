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
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.EQ.keyword(), ComparisonBlazeFilterOperatorStrategy.ComparisonMode.EQ),
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.NE.keyword(), ComparisonBlazeFilterOperatorStrategy.ComparisonMode.NOT_EQ),
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.NEQ.keyword(), ComparisonBlazeFilterOperatorStrategy.ComparisonMode.NOT_EQ),
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.GT.keyword(), ComparisonBlazeFilterOperatorStrategy.ComparisonMode.GT),
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.GE.keyword(), ComparisonBlazeFilterOperatorStrategy.ComparisonMode.GE),
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.GTE.keyword(), ComparisonBlazeFilterOperatorStrategy.ComparisonMode.GE),
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.LT.keyword(), ComparisonBlazeFilterOperatorStrategy.ComparisonMode.LT),
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.LE.keyword(), ComparisonBlazeFilterOperatorStrategy.ComparisonMode.LE),
        new ComparisonBlazeFilterOperatorStrategy(BlazeFilterOperators.LTE.keyword(), ComparisonBlazeFilterOperatorStrategy.ComparisonMode.LE),
        new BetweenBlazeFilterOperatorStrategy(BlazeFilterOperators.BETWEEN.keyword(), false),
        new BetweenBlazeFilterOperatorStrategy(BlazeFilterOperators.NOT_BETWEEN.keyword(), true),
        new BetweenBlazeFilterOperatorStrategy(BlazeFilterOperators.N_BETWEEN.keyword(), true),
        new InBlazeFilterOperatorStrategy(BlazeFilterOperators.IN.keyword(), false),
        new InBlazeFilterOperatorStrategy(BlazeFilterOperators.NOT_IN.keyword(), true),
        new InBlazeFilterOperatorStrategy(BlazeFilterOperators.NIN.keyword(), true),
        new LikeBlazeFilterOperatorStrategy(BlazeFilterOperators.LIKE.keyword(), false, true),
        new LikeBlazeFilterOperatorStrategy(BlazeFilterOperators.NOT_LIKE.keyword(), true, true),
        new LikeBlazeFilterOperatorStrategy(BlazeFilterOperators.ILIKE.keyword(), false, false),
        new LikeBlazeFilterOperatorStrategy(BlazeFilterOperators.NOT_ILIKE.keyword(), true, false),
        new NullityBlazeFilterOperatorStrategy(BlazeFilterOperators.IS_NULL.keyword(), false),
        new NullityBlazeFilterOperatorStrategy(BlazeFilterOperators.IS_NOT_NULL.keyword(), true)
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
