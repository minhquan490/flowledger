package io.flowledger.platform.query.annotation;

import io.flowledger.platform.query.mapping.AnnotationValue;
import io.flowledger.platform.query.mapping.MappingExpression;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a mapping method as a {@code COUNT} aggregation.
 *
 * <p>This annotation is discovered by {@code MappingAnnotationParser} through
 * {@link MappingExpression @MappingExpression("COUNT")}.
 *
 * <p>Example:
 * <pre>{@code
 * public interface LedgerView extends BlazeView<LedgerRow> {
 *   @Count("id")
 *   long totalRows();
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@MappingExpression("COUNT(%s)")
@AnnotationValue
public @interface Count {
  @AliasFor(annotation = AnnotationValue.class, attribute = AnnotationValue.VALUE_METHOD_NAME)
  String value();
}
