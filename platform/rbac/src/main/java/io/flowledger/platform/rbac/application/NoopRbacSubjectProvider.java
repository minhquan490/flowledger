package io.flowledger.platform.rbac.application;

import java.util.Optional;
import java.util.UUID;
import lombok.NoArgsConstructor;

/**
 * Default subject provider that returns no authenticated subject.
 */
@NoArgsConstructor
public class NoopRbacSubjectProvider implements RbacSubjectProvider {

  /**
   * Returns an empty subject identifier.
   *
   * @return an empty optional
   */
  @Override
  public Optional<UUID> currentSubjectId() {
    return Optional.empty();
  }
}
