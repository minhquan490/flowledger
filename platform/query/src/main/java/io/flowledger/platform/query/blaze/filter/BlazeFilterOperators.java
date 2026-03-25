package io.flowledger.platform.query.blaze.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.flowledger.platform.query.QuerySystemException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Enumerates supported filter operators and helper sets.
 */
public enum BlazeFilterOperators {
  EQ("eq"),
  NE("ne"),
  NEQ("neq"),
  GT("gt"),
  GE("ge"),
  GTE("gte"),
  LT("lt"),
  LE("le"),
  LTE("lte"),
  BETWEEN("between"),
  NOT_BETWEEN("notbetween"),
  N_BETWEEN("nbetween"),
  IN("in"),
  NOT_IN("notin"),
  NIN("nin"),
  LIKE("like"),
  NOT_LIKE("notlike"),
  ILIKE("ilike"),
  NOT_ILIKE("notilike"),
  IS_NULL("isnull"),
  IS_NOT_NULL("isnotnull");

  public static final Set<BlazeFilterOperators> VALUE_REQUIRED_OPERATORS = Set.of(
      EQ, NE, NEQ, GT, GE, GTE, LT, LE, LTE,
      IN, NOT_IN, NIN, LIKE, NOT_LIKE, ILIKE, NOT_ILIKE,
      BETWEEN, NOT_BETWEEN, N_BETWEEN
  );
  public static final Set<BlazeFilterOperators> VALUE_OPTIONAL_OPERATORS = Set.of(IS_NULL, IS_NOT_NULL);
  public static final Set<BlazeFilterOperators> SUPPORTED_OPERATORS = Set.copyOf(Set.of(values()));
  public static final Set<BlazeFilterOperators> BETWEEN_OPERATORS = Set.of(BETWEEN, NOT_BETWEEN, N_BETWEEN);
  private static final Map<String, BlazeFilterOperators> OPERATORS_BY_KEYWORD = Arrays.stream(values())
      .collect(Collectors.toUnmodifiableMap(BlazeFilterOperators::keyword, operator -> operator));
  private static final Set<String> SUPPORTED_OPERATOR_KEYWORDS = Arrays.stream(values())
      .map(BlazeFilterOperators::keyword)
      .collect(Collectors.toUnmodifiableSet());

  private final String keyword;

  BlazeFilterOperators(String keyword) {
    this.keyword = keyword;
  }

  /**
   * Returns the canonical lowercase keyword for the operator.
   *
   * @return canonical operator keyword
   */
  @JsonValue
  public String keyword() {
    return keyword;
  }

  /**
   * Creates an operator enum from a raw operator keyword.
   *
   * @param operator raw operator keyword
   * @return matching operator enum
   */
  @JsonCreator
  public static BlazeFilterOperators fromOperator(String operator) {
    String normalizedOperator = normalize(operator);
    return OPERATORS_BY_KEYWORD.get(normalizedOperator);
  }

  /**
   * Normalizes an operator to lower-case and validates support.
   *
   * @param operator raw operator
   * @return normalized operator
   */
  public static String normalize(String operator) {
    if (operator == null || operator.isBlank()) {
      throw new QuerySystemException("Filter operator must be a non-empty string.");
    }
    String normalizedOperator = operator.toLowerCase(Locale.ROOT);
    if (!SUPPORTED_OPERATOR_KEYWORDS.contains(normalizedOperator)) {
      throw new QuerySystemException("Unsupported filter operator: " + operator);
    }
    return normalizedOperator;
  }
}
