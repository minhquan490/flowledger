package io.flowledger.platform.query.annotation;

import io.flowledger.platform.query.mapping.AnnotationValue;
import io.flowledger.platform.query.mapping.MappingExpression;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a mapping method as a {@code COUNT DISTINCT} aggregation.
 *
 * <p>This annotation is discovered by {@code MappingAnnotationParser} through
 * {@link MappingExpression @MappingExpression("COUNT DISTINCT")}.
 *
 * <p>Example:
 * <pre>{@code
 * public interface LedgerView extends BlazeView<LedgerRow> {
 *   @CountDistinct("accountId")
 *   long distinctAccounts();
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@MappingExpression("COUNT(DISTINCT %s)")
@AnnotationValue
public @interface CountDistinct {
  @AliasFor(annotation = AnnotationValue.class, attribute = AnnotationValue.VALUE_METHOD_NAME)
  String value();
}
