package io.flowledger.platform.query.annotation;

import io.flowledger.platform.query.mapping.AnnotationValue;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sets an {@code OFFSET} on a mapped query.
 *
 * <p>Example:
 * <pre>{@code
 * public interface LedgerView extends BlazeView<LedgerRow> {
 *   @Offset(100)
 *   List<LedgerRow> pagedRows();
 * }
 * }</pre>
 *
 * <p>Type-level example:
 * <pre>{@code
 * @Offset(100)
 * public interface PagedLedgerView extends BlazeView<LedgerRow> {
 *   List<LedgerRow> pagedRows();
 * }
 * }</pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AnnotationValue
public @interface Offset {
  @AliasFor(annotation = AnnotationValue.class, attribute = AnnotationValue.VALUE_METHOD_NAME)
  int value();
}
