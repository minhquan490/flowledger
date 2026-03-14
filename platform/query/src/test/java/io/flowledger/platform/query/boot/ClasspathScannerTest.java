package io.flowledger.platform.query.boot;

import io.flowledger.core.boot.ClasspathScanner;
import io.flowledger.platform.query.test.DummyClass;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link ClasspathScanner}.
 */
class ClasspathScannerTest {

  /**
   * Verifies that scanning a known package returns the dummy class.
   */
  @Test
  void scanFindsClassesInPackage() {
    ClasspathScanner scanner = new ClasspathScanner();

    Set<Class<?>> result = scanner.scan(new String[]{"io.flowledger.platform.query.test"});

    assertTrue(result.contains(DummyClass.class));
  }

  /**
   * Verifies that null input returns an empty result set.
   */
  @Test
  void scanReturnsEmptyForNullInput() {
    ClasspathScanner scanner = new ClasspathScanner();

    Set<Class<?>> result = scanner.scan(null);

    assertTrue(result.isEmpty());
  }

  /**
   * Verifies that empty input returns an empty result set.
   */
  @Test
  void scanReturnsEmptyForEmptyInput() {
    ClasspathScanner scanner = new ClasspathScanner();

    Set<Class<?>> result = scanner.scan(new String[0]);

    assertTrue(result.isEmpty());
  }

  /**
   * Verifies that scanning unrelated packages does not include the dummy class.
   */
  @Test
  void scanDoesNotIncludeClassesOutsidePackage() {
    ClasspathScanner scanner = new ClasspathScanner();

    Set<Class<?>> result = scanner.scan(new String[]{"io.flowledger.core.boot"});

    assertFalse(result.contains(DummyClass.class));
  }
}
