package io.flowledger.platform.rbac.infrastructure.graphql;

import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;
import io.flowledger.platform.rbac.application.service.RbacFieldPermissionService;
import io.flowledger.platform.rbac.application.service.RbacPermissionService;
import io.flowledger.platform.rbac.domain.RbacAction;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link RbacGraphQLMutationPolicy}.
 */
class RbacGraphQLMutationPolicyTest {

  /**
   * Verifies create actions enforce permission and field validation.
   */
  @Test
  void validateWriteAccessForCreate() {
    RbacPermissionService permissionService = Mockito.mock(RbacPermissionService.class);
    RbacFieldPermissionService fieldPermissionService = Mockito.mock(RbacFieldPermissionService.class);
    RbacGraphQLMutationPolicy policy = new RbacGraphQLMutationPolicy(permissionService, fieldPermissionService);

    GraphQlMutationRequest request = new GraphQlMutationRequest(
        "account",
        "create",
        Map.of(),
        Map.of("name", "Main")
    );

    policy.validateWriteAccess("account", request);

    verify(permissionService).assertHasPermission("account", RbacAction.CREATE);
    verify(fieldPermissionService).validateWritableFields("account", List.of("name"));
  }

  /**
   * Verifies delete actions enforce permission without field validation.
   */
  @Test
  void validateWriteAccessForDelete() {
    RbacPermissionService permissionService = Mockito.mock(RbacPermissionService.class);
    RbacFieldPermissionService fieldPermissionService = Mockito.mock(RbacFieldPermissionService.class);
    RbacGraphQLMutationPolicy policy = new RbacGraphQLMutationPolicy(permissionService, fieldPermissionService);

    GraphQlMutationRequest request = new GraphQlMutationRequest(
        "account",
        "delete",
        Map.of("id", "acc_1"),
        Map.of()
    );

    policy.validateWriteAccess("account", request);

    verify(permissionService).assertHasPermission("account", RbacAction.DELETE);
    Mockito.verifyNoInteractions(fieldPermissionService);
  }

  /**
   * Verifies invalid actions are rejected.
   */
  @Test
  void validateWriteAccessRejectsUnsupportedAction() {
    RbacPermissionService permissionService = Mockito.mock(RbacPermissionService.class);
    RbacFieldPermissionService fieldPermissionService = Mockito.mock(RbacFieldPermissionService.class);
    RbacGraphQLMutationPolicy policy = new RbacGraphQLMutationPolicy(permissionService, fieldPermissionService);

    GraphQlMutationRequest request = new GraphQlMutationRequest(
        "account",
        "merge",
        Map.of(),
        Map.of()
    );

    GraphQlApiException ex = assertThrows(GraphQlApiException.class, () -> policy.validateWriteAccess("account", request));

    assertEquals(400, ex.getStatusCode());
  }
}
