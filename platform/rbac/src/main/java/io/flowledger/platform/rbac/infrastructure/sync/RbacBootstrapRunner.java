package io.flowledger.platform.rbac.infrastructure.sync;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * Runs RBAC bootstrapping tasks on application startup.
 */
public class RbacBootstrapRunner implements ApplicationRunner {
  private final RbacResourceSynchronizer synchronizer;

  /**
   * Creates a bootstrap runner.
   *
   * @param synchronizer the synchronizer to run
   */
  public RbacBootstrapRunner(RbacResourceSynchronizer synchronizer) {
    this.synchronizer = synchronizer;
  }

  /**
   * Executes the RBAC bootstrap routine.
   *
   * @param args application arguments
   */
  @Override
  public void run(@NonNull ApplicationArguments args) {
    synchronizer.synchronize();
  }
}
