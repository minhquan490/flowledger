package io.flowledger.platform.query.annotation;

import io.flowledger.platform.query.mapping.AnnotationValue;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a {@code HAVING} clause fragment for a mapped query.
 *
 * <p>Example:
 * <pre>{@code
 * public interface LedgerView extends BlazeView<LedgerRow> {
 *   @Having("SUM(amount) > 0")
 *   List<LedgerRow> positiveTotals();
 * }
 * }</pre>
 *
 * <p>Type-level example:
 * <pre>{@code
 * @Having("SUM(amount) > 0")
 * public interface PositiveLedgerView extends BlazeView<LedgerRow> {
 *   List<LedgerRow> positiveTotals();
 * }
 * }</pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AnnotationValue
public @interface Having {
  @AliasFor(annotation = AnnotationValue.class, attribute = AnnotationValue.VALUE_METHOD_NAME)
  String value();
}
