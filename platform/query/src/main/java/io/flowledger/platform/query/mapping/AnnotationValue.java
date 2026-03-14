package io.flowledger.platform.query.mapping;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an annotation as carrying a value used for mapping expressions.
 *
 * <p>Use this as a meta-annotation on other annotation types so
 * {@code MappingExpressionResolver} can extract their {@code value()}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface AnnotationValue {
  String value() default StringUtils.EMPTY;

  String VALUE_METHOD_NAME = "value";
}
