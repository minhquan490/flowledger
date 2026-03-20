package io.flowledger.platform.query.blaze.filter;

/**
 * Shared filter JSON syntax keys.
 */
public final class BlazeFilterSyntax {
  public static final String LOGICAL_OR = "or";
  public static final String LOGICAL_OR_ALIAS = "$or";
  public static final String OPERATOR_KEY = "op";
  public static final String OPERATOR_VALUE_KEY = "value";
  public static final String BETWEEN_FROM_KEY = "from";
  public static final String BETWEEN_TO_KEY = "to";

  /**
   * Utility class.
   */
  private BlazeFilterSyntax() {
  }
}
