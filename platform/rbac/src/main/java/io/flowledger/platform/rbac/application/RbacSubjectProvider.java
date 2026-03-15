package io.flowledger.platform.rbac.application;

import java.util.Optional;
import java.util.UUID;

/**
 * Provides the current subject identifier for RBAC evaluation.
 */
public interface RbacSubjectProvider {

  /**
   * Returns the current subject identifier if available.
   *
   * @return the optional subject identifier
   */
  Optional<UUID> currentSubjectId();
}
