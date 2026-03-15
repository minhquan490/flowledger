package io.flowledger.platform.rbac.infrastructure.graphql;

import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.graphql.application.GraphQLMutationPolicy;
import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;
import io.flowledger.platform.rbac.application.service.RbacFieldPermissionService;
import io.flowledger.platform.rbac.application.service.RbacPermissionService;
import io.flowledger.platform.rbac.domain.RbacAction;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * GraphQL mutation policy enforcing RBAC write rules.
 */
@Component
@RequiredArgsConstructor
public class RbacGraphQLMutationPolicy implements GraphQLMutationPolicy {
  private static final int BAD_REQUEST_STATUS = 400;

  private final RbacPermissionService permissionService;
  private final RbacFieldPermissionService fieldPermissionService;

  /**
   * Validates whether the caller can perform the mutation request.
   *
   * @param resource the resource name
   * @param request the mutation request
   */
  @Override
  public void validateWriteAccess(String resource, GraphQlMutationRequest request) {
    if (request == null || request.action() == null) {
      throw new GraphQlApiException("Mutation request must include an action.", BAD_REQUEST_STATUS);
    }
    RbacAction action = resolveAction(request.action());
    permissionService.assertHasPermission(resource, action);
    if (action == RbacAction.CREATE || action == RbacAction.UPDATE) {
      List<String> fields = request.data() == null ? List.of() : request.data().keySet().stream().toList();
      fieldPermissionService.validateWritableFields(resource, fields);
    }
  }

  /**
   * Resolves an RBAC action from the GraphQL mutation action.
   *
   * @param action the mutation action
   * @return the resolved RBAC action
   */
  private RbacAction resolveAction(String action) {
    String normalized = action == null ? "" : action.trim().toUpperCase(Locale.ROOT);
    return switch (normalized) {
      case "CREATE" -> RbacAction.CREATE;
      case "UPDATE" -> RbacAction.UPDATE;
      case "DELETE" -> RbacAction.DELETE;
      default -> throw new GraphQlApiException("Unsupported mutation action: " + action, BAD_REQUEST_STATUS);
    };
  }
}
