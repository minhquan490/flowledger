package io.flowledger.platform.rbac.infrastructure.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for RBAC infrastructure behavior.
 *
 * <p>Maps properties under {@code app.rbac} to a typed configuration model.
 */
@ConfigurationProperties(prefix = RbacProperties.PREFIX)
@Data
public class RbacProperties {

  /**
   * Property prefix for RBAC configuration.
   */
  public static final String PREFIX = "app.rbac";

  /**
   * Enables or disables RBAC infrastructure features.
   */
  private boolean enabled = true;
}
