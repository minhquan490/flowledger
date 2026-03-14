package io.flowledger.platform.query.blaze;

import io.flowledger.core.boot.ClasspathScanner;
import io.flowledger.platform.query.test.DummyView;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link BlazeViewLoader}.
 */
class BlazeViewLoaderTest {

  /**
   * Verifies that Blaze view classes are discovered by package scan.
   */
  @Test
  void loadViewsFindsEntityViews() {
    BlazeViewLoader loader = new BlazeViewLoader(new ClasspathScanner());

    Collection<Class<?>> views = loader.loadViews(new String[]{"io.flowledger.platform.query.test"});

    assertTrue(views.contains(DummyView.class));
  }
}
