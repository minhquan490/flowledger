package io.flowledger.core.boot;

/**
 * Cache names used only during application bootstrap.
 */
public final class BootstrapCacheNames {
  /**
   * Cache storing classpath scan results for bootstrap package scanning.
   */
  public static final String CLASSPATH_SCANNER_SCAN = "classpathScanner.scan";

  private BootstrapCacheNames() {
    throw new IllegalStateException("Utility class");
  }
}
