package io.flowledger.platform.query.blaze;

import com.blazebit.persistence.view.Mapping;
import io.flowledger.platform.query.QuerySystemException;
import io.flowledger.platform.query.mapping.MappingExpressionResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Resolves mapping annotations declared on Blaze view methods into expression strings.
 *
 * <p>Each view method may declare at most one query annotation. The resulting map
 * contains method names mapped to their resolved expressions.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnMissingBean(BlazeMappingResolver.class)
public class BlazeMappingResolver {
  private static final String DUPLICATE_MAPPING_ERROR =
      "Each view method must declare at most one query annotation.";

  private final MappingExpressionResolver mappingExpressionResolver;

  /**
   * Builds a mapping of view method names to resolved query expressions.
   *
   * @param viewClass the Blaze view interface to inspect
   * @return a non-null map of method names to expressions
   * @throws QuerySystemException when a method declares more than one query annotation
   */
  public Map<String, String> resolveMappings(Class<?> viewClass) {
    if (viewClass == null) {
      return Map.of();
    }

    Map<String, String> mappings = new HashMap<>();
    for (Method method : viewClass.getMethods()) {
      resolveMethodExpression(method).ifPresent(expression -> {
        String methodName = method.getName();
        if (mappings.containsKey(methodName)) {
          throw new QuerySystemException(DUPLICATE_MAPPING_ERROR);
        }
        mappings.put(methodName, expression);
      });
    }

    return mappings;
  }

  /**
   * Resolves the first query expression found on the given method.
   *
   * @param method the method to inspect
   * @return an {@link Optional} containing the resolved expression when present
   */
  private Optional<String> resolveMethodExpression(Method method) {
    Optional<String> resolved = Optional.empty();
    for (Annotation annotation : getMethodAnnotations(method)) {
      if (annotation instanceof Mapping) {
        continue;
      }
      Optional<String> expression = mappingExpressionResolver.resolve(annotation);
      if (expression.isPresent()) {
        if (resolved.isPresent()) {
          throw new QuerySystemException(DUPLICATE_MAPPING_ERROR);
        }
        resolved = expression;
      }
    }
    return resolved;
  }

  /**
   * Resolves annotations from the bridged method to handle generics.
   *
   * @param method the method to inspect
   * @return the resolved annotations on the bridged method
   */
  private Annotation[] getMethodAnnotations(Method method) {
    return BridgeMethodResolver.findBridgedMethod(method).getAnnotations();
  }
}
