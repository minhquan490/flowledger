package io.flowledger.platform.query.blaze.filter;

import io.flowledger.platform.query.QuerySystemException;
import java.util.Locale;
import java.util.Set;

/**
 * Shared filter operator constants and helper sets.
 */
public final class BlazeFilterOperators {
  public static final String EQ = "eq";
  public static final String NE = "ne";
  public static final String NEQ = "neq";
  public static final String GT = "gt";
  public static final String GE = "ge";
  public static final String GTE = "gte";
  public static final String LT = "lt";
  public static final String LE = "le";
  public static final String LTE = "lte";
  public static final String BETWEEN = "between";
  public static final String NOT_BETWEEN = "notbetween";
  public static final String N_BETWEEN = "nbetween";
  public static final String IN = "in";
  public static final String NOT_IN = "notin";
  public static final String NIN = "nin";
  public static final String LIKE = "like";
  public static final String NOT_LIKE = "notlike";
  public static final String ILIKE = "ilike";
  public static final String NOT_ILIKE = "notilike";
  public static final String IS_NULL = "isnull";
  public static final String IS_NOT_NULL = "isnotnull";

  public static final Set<String> VALUE_REQUIRED_OPERATORS = Set.of(
      EQ, NE, NEQ, GT, GE, GTE, LT, LE, LTE,
      IN, NOT_IN, NIN, LIKE, NOT_LIKE, ILIKE, NOT_ILIKE,
      BETWEEN, NOT_BETWEEN, N_BETWEEN
  );
  public static final Set<String> VALUE_OPTIONAL_OPERATORS = Set.of(IS_NULL, IS_NOT_NULL);
  public static final Set<String> SUPPORTED_OPERATORS = Set.of(
      EQ, NE, NEQ, GT, GE, GTE, LT, LE, LTE,
      IN, NOT_IN, NIN, LIKE, NOT_LIKE, ILIKE, NOT_ILIKE,
      IS_NULL, IS_NOT_NULL, BETWEEN, NOT_BETWEEN, N_BETWEEN
  );
  public static final Set<String> BETWEEN_OPERATORS = Set.of(BETWEEN, NOT_BETWEEN, N_BETWEEN);

  /**
   * Utility class.
   */
  private BlazeFilterOperators() {
  }

  /**
   * Normalizes an operator to lower-case.
   *
   * @param operator raw operator
   * @return normalized operator
   */
  public static String normalize(String operator) {
    if (operator == null || operator.isBlank()) {
      throw new QuerySystemException("Filter operator must be a non-empty string.");
    }
    return operator.toLowerCase(Locale.ROOT);
  }
}
