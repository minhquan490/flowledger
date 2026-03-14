package io.flowledger.platform.query.mapping;

import io.flowledger.platform.query.annotation.Sum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta-annotation for mapping annotations consumed by {@code MappingAnnotationParser}.
 *
 * <p>Apply this only to other annotation types (for example {@link Sum}) to declare
 * the SQL/expression keyword that annotation represents.
 *
 * <p>Example:
 * <pre>{@code
 * @Retention(RetentionPolicy.RUNTIME)
 * @Target(ElementType.METHOD)
 * @MappingExpression("SUM(%s)")
 * public @interface Sum {
 *   String value();
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface MappingExpression {
  /**
   * Expression keyword used by {@code MappingAnnotationParser}.
   *
   * <p>Example: {@code @MappingExpression("SUM")} on {@link Sum}.
   */
  String value();
}
