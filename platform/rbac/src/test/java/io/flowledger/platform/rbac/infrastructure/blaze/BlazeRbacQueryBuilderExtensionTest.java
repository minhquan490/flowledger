package io.flowledger.platform.rbac.infrastructure.blaze;

import io.flowledger.platform.rbac.application.context.RbacRequestContext;
import io.flowledger.platform.rbac.application.context.RbacRequestContextHolder;
import io.flowledger.platform.rbac.application.service.RbacRowFilterService;
import io.flowledger.platform.rbac.domain.role.valueobject.RbacAction;
import io.flowledger.platform.rbac.infrastructure.autoconfigure.RbacProperties;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link BlazeRbacQueryBuilderExtension}.
 */
class BlazeRbacQueryBuilderExtensionTest {

  /**
   * Verifies row filters are merged when a request context is present.
   */
  @Test
  void customizeFiltersMergesRowFilters() {
    RbacRowFilterService rowFilterService = Mockito.mock(RbacRowFilterService.class);
    BlazeRbacQueryBuilderExtension extension = new BlazeRbacQueryBuilderExtension(rowFilterService, enabledProperties());

    RbacRequestContextHolder.set(new RbacRequestContext("account", RbacAction.READ));
    when(rowFilterService.resolveRowFilters("account")).thenReturn(Map.of("status", "ACTIVE"));

    Map<String, Object> result = extension.customizeFilters(Map.of("type", "BANK"));

    assertEquals(Map.of("type", "BANK", "status", "ACTIVE"), result);
    assertTrue(RbacRequestContextHolder.get().isEmpty());
  }

  /**
   * Verifies filters are untouched when no request context exists.
   */
  @Test
  void customizeFiltersWithoutContextReturnsInput() {
    RbacRowFilterService rowFilterService = Mockito.mock(RbacRowFilterService.class);
    BlazeRbacQueryBuilderExtension extension = new BlazeRbacQueryBuilderExtension(rowFilterService, enabledProperties());

    Map<String, Object> result = extension.customizeFilters(Map.of("type", "BANK"));

    assertEquals(Map.of("type", "BANK"), result);
  }

  /**
   * Creates properties with RBAC enabled.
   *
   * @return RBAC properties
   */
  private RbacProperties enabledProperties() {
    RbacProperties properties = new RbacProperties();
    properties.setEnabled(true);
    return properties;
  }
}
