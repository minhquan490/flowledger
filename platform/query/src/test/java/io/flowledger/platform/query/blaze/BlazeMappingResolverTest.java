package io.flowledger.platform.query.blaze;

import io.flowledger.platform.query.QuerySystemException;
import io.flowledger.platform.query.annotation.Max;
import io.flowledger.platform.query.annotation.Sum;
import io.flowledger.platform.query.mapping.MappingExpressionResolver;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link BlazeMappingResolver}.
 */
class BlazeMappingResolverTest {

  /**
   * Verifies that a single mapping annotation resolves to a method mapping.
   */
  @Test
  void resolvesSingleMethodMapping() {
    BlazeMappingResolver resolver = new BlazeMappingResolver(new MappingExpressionResolver());

    Map<String, String> mappings = resolver.resolveMappings(SingleMappingView.class);

    assertEquals(Map.of("total", "SUM(amount)"), mappings);
  }

  /**
   * Verifies that multiple mapping annotations on one method are rejected.
   */
  @Test
  void rejectsMultipleMappingsOnOneMethod() {
    BlazeMappingResolver resolver = new BlazeMappingResolver(new MappingExpressionResolver());

    assertThrows(QuerySystemException.class, () -> resolver.resolveMappings(DuplicateMappingView.class));
  }

  /**
   * Test view with one mapping annotation.
   */
  private interface SingleMappingView {
    /**
     * Returns a SUM mapping.
     *
     * @return mapping expression
     */
    @Sum("amount")
    String total();
  }

  /**
   * Test view with duplicate mapping annotations.
   */
  private interface DuplicateMappingView {
    /**
     * Returns a method with two mapping annotations.
     *
     * @return mapping expression
     */
    @Sum("amount")
    @Max("amount")
    String total();
  }
}
