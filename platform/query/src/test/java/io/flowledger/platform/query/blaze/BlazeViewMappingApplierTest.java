package io.flowledger.platform.query.blaze;

import com.blazebit.persistence.view.EntityViews;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import io.flowledger.platform.query.mapping.MappingExpressionResolver;
import io.flowledger.platform.query.test.DummyView;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for {@link BlazeViewMappingApplier}.
 */
class BlazeViewMappingApplierTest {

  /**
   * Verifies that {@link Mapping} is injected for custom annotations.
   */
  @Test
  void applyInjectsMappingAnnotation() throws Exception {
    BlazeMappingResolver resolver = new BlazeMappingResolver(new MappingExpressionResolver());
    BlazeViewMappingApplier applier = new BlazeViewMappingApplier(resolver);
    EntityViewConfiguration config = EntityViews.createDefaultConfiguration();

    applier.apply(DummyView.class, config);

    boolean found = false;
    for (Class<?> view : config.getEntityViews()) {
      Method method = view.getMethod("totalAmount");
      Mapping mapping = method.getAnnotation(Mapping.class);
      if (mapping != null && "SUM(amount)".equals(mapping.value())) {
        found = true;
        break;
      }
    }

    assertNotNull(config.getEntityViews());
    assertEquals(true, found);
  }
}
