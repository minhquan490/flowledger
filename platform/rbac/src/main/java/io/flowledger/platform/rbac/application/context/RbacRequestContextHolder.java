package io.flowledger.platform.rbac.application.context;

import java.util.Optional;

/**
 * Holds RBAC request context in a thread-local scope.
 */
public final class RbacRequestContextHolder {
  private static final ThreadLocal<RbacRequestContext> CONTEXT = new ThreadLocal<>();

  /**
   * Prevents instantiation of this utility class.
   */
  private RbacRequestContextHolder() {
  }

  /**
   * Sets the current RBAC request context.
   *
   * @param context the context to set
   */
  public static void set(RbacRequestContext context) {
    CONTEXT.set(context);
  }

  /**
   * Returns the current RBAC request context.
   *
   * @return the optional context
   */
  public static Optional<RbacRequestContext> get() {
    return Optional.ofNullable(CONTEXT.get());
  }

  /**
   * Clears the current RBAC request context.
   */
  public static void clear() {
    CONTEXT.remove();
  }
}
