package io.flowledger.platform.rbac.infrastructure.graphql;

import io.flowledger.platform.graphql.application.GraphQlAccessPolicy;
import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.graphql.domain.GraphQlReadRequest;
import io.flowledger.platform.graphql.domain.GraphQlSearchRequest;
import io.flowledger.core.web.HttpStatusCodes;
import io.flowledger.platform.rbac.application.context.RbacRequestContext;
import io.flowledger.platform.rbac.application.context.RbacRequestContextHolder;
import io.flowledger.platform.rbac.application.service.RbacFieldPermissionService;
import io.flowledger.platform.rbac.application.service.RbacPermissionService;
import io.flowledger.platform.rbac.domain.role.valueobject.RbacAction;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * GraphQL access policy enforcing RBAC read rules and field filtering.
 */
@Component
@RequiredArgsConstructor
public class RbacGraphQlAccessPolicy implements GraphQlAccessPolicy {
  private final RbacPermissionService permissionService;
  private final RbacFieldPermissionService fieldPermissionService;

  /**
   * Applies RBAC rules to a read request.
   *
   * @param request the incoming read request
   * @return the authorized read request
   */
  @Override
  public GraphQlReadRequest authorizeRead(GraphQlReadRequest request) {
    String resource = request.model();
    permissionService.assertHasPermission(resource, RbacAction.READ);
    List<String> filtered = filterReadableFields(resource, request.fields());
    return new GraphQlReadRequest(request.model(), request.key(), filtered);
  }

  /**
   * Applies RBAC rules to a search request.
   *
   * @param request the incoming search request
   * @return the authorized search request
   */
  @Override
  public GraphQlSearchRequest authorizeSearch(GraphQlSearchRequest request) {
    String resource = request.model();
    permissionService.assertHasPermission(resource, RbacAction.READ);
    RbacRequestContextHolder.set(new RbacRequestContext(resource, RbacAction.READ));
    List<String> filtered = filterReadableFields(resource, request.fields());
    return new GraphQlSearchRequest(
        request.model(),
        request.filter(),
        request.page(),
        request.sort(),
        filtered
    );
  }

  /**
   * Filters requested fields to those the subject can read.
   *
   * @param resource the resource name
   * @param requested the requested fields
   * @return the filtered fields
   */
  private List<String> filterReadableFields(String resource, List<String> requested) {
    List<String> allowed = fieldPermissionService.readableFields(resource);
    if (allowed.isEmpty()) {
      throw new GraphQlApiException(
          "No readable fields available for " + resource + ".",
          HttpStatusCodes.FORBIDDEN
      );
    }
    if (requested == null || requested.isEmpty()) {
      return allowed;
    }
    List<String> filtered = requested.stream()
        .filter(allowed::contains)
        .toList();
    if (filtered.isEmpty()) {
      throw new GraphQlApiException(
          "No readable fields available for " + resource + ".",
          HttpStatusCodes.FORBIDDEN
      );
    }
    return filtered;
  }
}
