package io.flowledger.platform.query.mapping;

import io.flowledger.platform.query.QuerySystemException;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Resolves a mapping annotation into a SQL expression string.
 *
 * <p>If the annotation is meta-annotated with {@link MappingExpression}, its
 * template is formatted using the annotation's {@code value()}. Otherwise, the
 * raw {@code value()} is returned.
 */
@Component
public class MappingExpressionResolver {

  public static final String CACHE_NAME = "mappingExpressionResolver";

  /**
   * Resolves the mapping expression for the given annotation.
   *
   * @param annotation the annotation to resolve
   * @return an {@link Optional} containing the resolved expression, or empty if the annotation is null
   * @throws QuerySystemException when the annotation is missing {@link AnnotationValue}
   */
  @Cacheable(cacheNames = CACHE_NAME, key = "#annotation.annotationType().getName()")
  public <T extends Annotation> Optional<String> resolve(T annotation) {
    if (annotation == null || !canResolve(annotation)) {
      return Optional.empty();
    }
    AnnotationValue annotationValue = requireAnnotationValue(annotation);
    MappingExpression expression = AnnotationUtils.getAnnotation(annotation, MappingExpression.class);
    return Optional.of(formatExpression(annotationValue.value(), expression));
  }

  public boolean canResolve(Annotation annotation) {
    return annotation.annotationType().isAnnotationPresent(AnnotationValue.class);
  }

  /**
   * Ensures that the given annotation is meta-annotated with {@link AnnotationValue}.
   *
   * @param annotation the annotation to inspect
   * @return the resolved {@link AnnotationValue}
   */
  private AnnotationValue requireAnnotationValue(Annotation annotation) {
    try {
      return MergedAnnotations.from(annotation)
          .get(AnnotationValue.class)
          .synthesize();
    } catch (NoSuchElementException e) {
      throw new QuerySystemException(e.getMessage(), e);
    }
  }

  /**
   * Formats the final expression using the provided template.
   *
   * @param value the raw annotation value
   * @param expression the expression template metadata, or {@code null} when absent
   * @return the formatted expression string
   */
  private String formatExpression(String value, @Nullable MappingExpression expression) {
    if (expression == null) {
      return value;
    }
    return String.format(expression.value(), value);
  }
}
