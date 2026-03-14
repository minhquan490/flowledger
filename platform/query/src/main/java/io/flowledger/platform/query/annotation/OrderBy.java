package io.flowledger.platform.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares an {@code ORDER BY} clause for a mapped query.
 *
 * <p>Example:
 * <pre>{@code
 * public interface LedgerView extends BlazeView<LedgerRow> {
 *   @OrderBy(direction = OrderBy.Direction.DESC)
 *   List<LedgerRow> recentFirst();
 * }
 * }</pre>
 *
 * <p>Type-level example:
 * <pre>{@code
 * @OrderBy(direction = OrderBy.Direction.DESC)
 * public interface RecentLedgerView extends BlazeView<LedgerRow> {
 *   List<LedgerRow> recentFirst();
 * }
 * }</pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderBy {

  Direction direction() default Direction.ASC;

  enum Direction {
    ASC,
    DESC
  }
}
