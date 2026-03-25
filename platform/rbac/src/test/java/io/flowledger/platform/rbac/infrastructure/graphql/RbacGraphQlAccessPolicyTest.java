package io.flowledger.platform.rbac.infrastructure.graphql;

import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.graphql.domain.GraphQlReadRequest;
import io.flowledger.platform.rbac.application.service.RbacFieldPermissionService;
import io.flowledger.platform.rbac.application.service.RbacPermissionService;
import io.flowledger.platform.rbac.domain.role.valueobject.RbacAction;
import io.flowledger.platform.rbac.infrastructure.autoconfigure.RbacProperties;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link RbacGraphQlAccessPolicy}.
 */
class RbacGraphQlAccessPolicyTest {

  /**
   * Verifies readable fields are filtered when permissions are restricted.
   */
  @Test
  void authorizeReadFiltersFields() {
    RbacPermissionService permissionService = Mockito.mock(RbacPermissionService.class);
    RbacFieldPermissionService fieldPermissionService = Mockito.mock(RbacFieldPermissionService.class);
    RbacGraphQlAccessPolicy policy = new RbacGraphQlAccessPolicy(
        permissionService,
        fieldPermissionService,
        enabledProperties()
    );

    GraphQlReadRequest request = new GraphQlReadRequest("account", Map.of("id", "acc_1"), List.of("id", "name"));
    when(fieldPermissionService.readableFields("account")).thenReturn(List.of("id"));

    GraphQlReadRequest result = policy.authorizeRead(request);

    verify(permissionService).assertHasPermission("account", RbacAction.READ);
    assertEquals(List.of("id"), result.fields());
  }

  /**
   * Verifies access is denied when no readable fields remain.
   */
  @Test
  void authorizeReadRejectsEmptyFieldSelection() {
    RbacPermissionService permissionService = Mockito.mock(RbacPermissionService.class);
    RbacFieldPermissionService fieldPermissionService = Mockito.mock(RbacFieldPermissionService.class);
    RbacGraphQlAccessPolicy policy = new RbacGraphQlAccessPolicy(
        permissionService,
        fieldPermissionService,
        enabledProperties()
    );

    GraphQlReadRequest request = new GraphQlReadRequest("account", Map.of("id", "acc_1"), List.of("name"));
    when(fieldPermissionService.readableFields("account")).thenReturn(List.of("id"));

    GraphQlApiException ex = assertThrows(GraphQlApiException.class, () -> policy.authorizeRead(request));

    assertEquals(403, ex.getStatusCode());
  }

  /**
   * Verifies access is denied when no readable fields are granted for the resource.
   */
  @Test
  void authorizeReadRejectsWhenAllowedFieldsMissing() {
    RbacPermissionService permissionService = Mockito.mock(RbacPermissionService.class);
    RbacFieldPermissionService fieldPermissionService = Mockito.mock(RbacFieldPermissionService.class);
    RbacGraphQlAccessPolicy policy = new RbacGraphQlAccessPolicy(
        permissionService,
        fieldPermissionService,
        enabledProperties()
    );

    GraphQlReadRequest request = new GraphQlReadRequest("account", Map.of("id", "acc_1"), List.of("name"));
    when(fieldPermissionService.readableFields("account")).thenReturn(List.of());

    GraphQlApiException ex = assertThrows(GraphQlApiException.class, () -> policy.authorizeRead(request));

    assertEquals(403, ex.getStatusCode());
  }

  /**
   * Verifies read authorization is bypassed when RBAC is disabled.
   */
  @Test
  void authorizeReadReturnsInputWhenDisabled() {
    RbacPermissionService permissionService = Mockito.mock(RbacPermissionService.class);
    RbacFieldPermissionService fieldPermissionService = Mockito.mock(RbacFieldPermissionService.class);
    RbacGraphQlAccessPolicy policy = new RbacGraphQlAccessPolicy(
        permissionService,
        fieldPermissionService,
        disabledProperties()
    );

    GraphQlReadRequest request = new GraphQlReadRequest("account", Map.of("id", "acc_1"), List.of("id", "name"));

    GraphQlReadRequest result = policy.authorizeRead(request);

    assertEquals(request, result);
    Mockito.verifyNoInteractions(permissionService);
    Mockito.verifyNoInteractions(fieldPermissionService);
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

  /**
   * Creates properties with RBAC disabled.
   *
   * @return RBAC properties
   */
  private RbacProperties disabledProperties() {
    RbacProperties properties = new RbacProperties();
    properties.setEnabled(false);
    return properties;
  }
}
