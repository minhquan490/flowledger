package io.flowledger.platform.query.mapping;

import io.flowledger.platform.query.QuerySystemException;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link MappingExpressionResolver}.
 */
class MappingExpressionResolverTest {

  /**
   * Verifies that null annotations produce an empty optional.
   */
  @Test
  void resolveReturnsEmptyWhenAnnotationIsNull() {
    MappingExpressionResolver resolver = new MappingExpressionResolver();
    assertTrue(resolver.resolve(null).isEmpty());
  }

  /**
   * Verifies that annotations without {@link MappingExpression} return raw values.
   */
  @Test
  void resolveReturnsRawValueWhenNoMappingExpressionPresent() throws Exception {
    MappingExpressionResolver resolver = new MappingExpressionResolver();
    Method method = ValueOnlyView.class.getMethod("amount");
    ValueOnly valueOnly = method.getAnnotation(ValueOnly.class);

    Optional<String> resolved = resolver.resolve(valueOnly);

    assertEquals(Optional.of("amount"), resolved);
  }

  /**
   * Verifies that annotations with {@link MappingExpression} are formatted.
   */
  @Test
  void resolveFormatsExpressionWhenMappingExpressionPresent() throws Exception {
    MappingExpressionResolver resolver = new MappingExpressionResolver();
    Method method = SumView.class.getMethod("amount");
    SumLike sum = method.getAnnotation(SumLike.class);

    Optional<String> resolved = resolver.resolve(sum);

    assertEquals(Optional.of("SUM(amount)"), resolved);
  }

  /**
   * Verifies that missing {@link AnnotationValue} triggers a {@link QuerySystemException}.
   */
  @Test
  void resolveThrowsWhenAnnotationValueMissing() throws Exception {
    MappingExpressionResolver resolver = new MappingExpressionResolver();
    Method method = MissingValueView.class.getMethod("amount");
    MissingValue missingValue = method.getAnnotation(MissingValue.class);

    assertThrows(QuerySystemException.class, () -> resolver.resolve(missingValue));
  }

  /**
   * Test annotation without {@link MappingExpression}.
   */
  @Retention(RetentionPolicy.RUNTIME)
  @AnnotationValue
  @interface ValueOnly {
    /**
     * Returns the raw value.
     *
     * @return mapping value
     */
    @AliasFor(annotation = AnnotationValue.class, attribute = AnnotationValue.VALUE_METHOD_NAME)
    String value();
  }

  /**
   * Test annotation with {@link MappingExpression}.
   */
  @Retention(RetentionPolicy.RUNTIME)
  @AnnotationValue
  @MappingExpression("SUM(%s)")
  @interface SumLike {
    /**
     * Returns the raw value.
     *
     * @return mapping value
     */
    @AliasFor(annotation = AnnotationValue.class, attribute = AnnotationValue.VALUE_METHOD_NAME)
    String value();
  }

  /**
   * Test annotation missing {@link AnnotationValue}.
   */
  @Retention(RetentionPolicy.RUNTIME)
  @MappingExpression("SUM(%s)")
  @interface MissingValue {
    /**
     * Returns the raw value.
     *
     * @return mapping value
     */
    String value();
  }

  /**
   * Test view using {@link ValueOnly}.
   */
  private interface ValueOnlyView {
    /**
     * Returns a raw value-only mapping.
     *
     * @return raw mapping string
     */
    @ValueOnly("amount")
    String amount();
  }

  /**
   * Test view using {@link SumLike}.
   */
  private interface SumView {
    /**
     * Returns a SUM mapping.
     *
     * @return mapping expression
     */
    @SumLike("amount")
    String amount();
  }

  /**
   * Test view using {@link MissingValue}.
   */
  private interface MissingValueView {
    /**
     * Returns a mapping missing {@link AnnotationValue}.
     *
     * @return mapping expression
     */
    @MissingValue("amount")
    String amount();
  }
}
