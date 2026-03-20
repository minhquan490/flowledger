package io.flowledger.platform.query.blaze.filter;

import com.blazebit.persistence.BaseWhereBuilder;

/**
 * Strategy contract for applying a structured filter operator to a Blaze where builder.
 */
public interface BlazeFilterOperatorStrategy {

  /**
   * Returns the operator keyword handled by this strategy.
   *
   * @return the operator keyword
   */
  String operator();

  /**
   * Applies the operator-specific predicate to the provided where builder.
   *
   * @param whereBuilder the target where builder
   * @param field the field name
   * @param value the operator value
   */
  void apply(BaseWhereBuilder<?> whereBuilder, String field, Object value);
}
