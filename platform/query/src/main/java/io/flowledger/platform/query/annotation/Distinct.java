package io.flowledger.platform.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a mapped query as {@code DISTINCT}.
 *
 * <p>Example:
 * <pre>{@code
 * public interface LedgerView extends BlazeView<LedgerRow> {
 *   @Distinct
 *   List<LedgerRow> uniqueRows();
 * }
 * }</pre>
 *
 * <p>Type-level example:
 * <pre>{@code
 * @Distinct
 * public interface UniqueLedgerView extends BlazeView<LedgerRow> {
 *   List<LedgerRow> uniqueRows();
 * }
 * }</pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Distinct {}
